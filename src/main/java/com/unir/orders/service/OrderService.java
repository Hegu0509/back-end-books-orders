package com.unir.orders.service;

import com.unir.orders.client.CatalogueClient;
import com.unir.orders.client.model.BookCatalogueDto;
import com.unir.orders.controller.model.CreateOrderRequestDto;
import com.unir.orders.controller.model.GetOrdersResponseDto;
import com.unir.orders.controller.model.OrderItemRequestDto;
import com.unir.orders.controller.model.OrderResponseDto;
import com.unir.orders.exception.BookNotAvailableException;
import com.unir.orders.exception.OrderNotFoundException;
import com.unir.orders.repository.OrderJpaRepository;
import com.unir.orders.repository.model.Order;
import com.unir.orders.repository.model.OrderItem;
import com.unir.orders.utils.OrderMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final CatalogueClient catalogueClient;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {

        if (request == null || !StringUtils.hasText(request.getUserId())) {
            throw new IllegalArgumentException("The order must include a userId.");
        }
        if (CollectionUtils.isEmpty(request.getItems())) {
            throw new IllegalArgumentException("The order must include at least one item.");
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .status("CONFIRMED")
                .currency("EUR")
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto line : request.getItems()) {
            if (line.getBookId() == null) {
                throw new IllegalArgumentException("Each item must include a bookId.");
            }
            int quantity = line.getQuantity() == null ? 0 : line.getQuantity();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity for book " + line.getBookId() + " must be greater than zero.");
            }

            BookCatalogueDto book = fetchBook(line.getBookId());
            validateAvailability(book, quantity);

            BigDecimal unitPrice = book.getPrice() != null ? book.getPrice() : BigDecimal.ZERO;
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            total = total.add(subtotal);

            order.addItem(OrderItem.builder()
                    .bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .isbn(book.getIsbn())
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build());
        }

        order.setTotalAmount(total);
        return orderMapper.asOrderResponseDto(orderJpaRepository.save(order));
    }

    @Transactional(readOnly = true)
    public GetOrdersResponseDto getOrdersByUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("A userId is required to list orders.");
        }
        return orderMapper.asGetOrdersResponseDto(
                orderJpaRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long id) {
        Order order = orderJpaRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order with ID " + id + " not found."));
        return orderMapper.asOrderResponseDto(order);
    }

    private BookCatalogueDto fetchBook(Long bookId) {
        try {
            return catalogueClient.getBook(bookId);
        } catch (FeignException.NotFound ex) {
            throw new BookNotAvailableException("Book " + bookId + " does not exist in the catalogue.");
        }
    }

    private void validateAvailability(BookCatalogueDto book, int quantity) {
        if (book == null || book.getId() == null) {
            throw new BookNotAvailableException("The requested book does not exist in the catalogue.");
        }
        if (book.getVisible() != null && !book.getVisible()) {
            throw new BookNotAvailableException("Book " + book.getId() + " is not available (hidden).");
        }
        int stock = book.getStock() == null ? 0 : book.getStock();
        if (stock < quantity) {
            throw new BookNotAvailableException(
                    "Insufficient stock for book " + book.getId() + " (available: " + stock + ", requested: " + quantity + ").");
        }
    }
}
