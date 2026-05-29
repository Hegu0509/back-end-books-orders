package com.unir.orders.service;

import com.unir.orders.controller.model.CreateOrderRequestDto;
import com.unir.orders.controller.model.CreateOrderResponseDto;
import com.unir.orders.controller.model.RequestedBook;
import com.unir.orders.exception.BookNotFoundException;
import com.unir.orders.facade.BooksCatalogueFacade;
import com.unir.orders.facade.model.BookDto;
import com.unir.orders.repository.OrderJpaRepository;
import com.unir.orders.repository.model.Order;
import com.unir.orders.repository.model.OrderItem;
import com.unir.orders.repository.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CreateOrdersService {

    private static final String DEFAULT_CURRENCY = "EUR";

    private final BooksCatalogueFacade booksCatalogueFacade;
    private final OrderJpaRepository orderJpaRepository;

    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto request) {

        // Validar que la solicitud no esté vacía
        if (request.getBooks() == null || request.getBooks().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un producto");
        }

        Map<BookDto, OrderItem> bookOrderItemMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RequestedBook book : request.getBooks()) {
            BookDto bookData = getBookData(book);
            BigDecimal unitPrice = getUnitPrice(bookData);
            OrderItem orderItem = OrderItem.builder()
                    .bookId(book.getId())
                    .bookTitle(bookData.getTitle())
                    .isbn(bookData.getIsbn())
                    .quantity(book.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(getSubtotal(book, unitPrice))
                    .build();
            totalAmount = totalAmount.add(orderItem.getSubtotal());
            bookOrderItemMap.put(bookData, orderItem);
        }

        String orderName = generateOrderName();
        Order order = Order.builder()
                //.name(orderName)
                .createdAt(LocalDateTime.now())
                .totalAmount(totalAmount)
                .currency(DEFAULT_CURRENCY)
                .status(OrderStatus.EN_PROCESO.toString())
                .userId("1") // Se debe obtener del contexto de seguridad
                .items(bookOrderItemMap.values().stream().toList())
                .build();

        bookOrderItemMap.values().forEach(item -> item.setOrder(order));
        Order savedOrder = orderJpaRepository.save(order);

        for (Map.Entry<BookDto, OrderItem> entry : bookOrderItemMap.entrySet()) {
            updateBookStock(entry.getKey().getStock(), entry.getValue());
        }

        // Crear la respuesta
        return CreateOrderResponseDto.builder()
                .order(orderName  + "-" + savedOrder.getId())
                .build();
    }

    private BookDto getBookData(RequestedBook requestedBook) {
        if (requestedBook.getQuantity() == null || requestedBook.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0 para el producto ID: " + requestedBook.getId());
        }
        BookDto book = booksCatalogueFacade.getBook(requestedBook.getId());
        validateStock(requestedBook, book);
        return book;
    }

    private void validateStock(RequestedBook requestedBook, BookDto book) {
        if (book == null) {
            throw new BookNotFoundException("Producto no encontrado con ID: " + requestedBook.getId());
        }
        if (book.getStock() == null || book.getStock() < requestedBook.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + book.getTitle()+
                    ". Stock disponible: " + book.getStock() + ", solicitado: " + requestedBook.getQuantity());
        }
    }

    private BigDecimal getUnitPrice(BookDto book) {
        return book.getPrice() != null ? book.getPrice() : BigDecimal.ZERO;
    }

    private BigDecimal getSubtotal(RequestedBook requestedBook, BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(requestedBook.getQuantity()));
    }

    private String generateOrderName() {
        return "ORDER-" + System.currentTimeMillis();
    }

    private void updateBookStock(Integer currentStock, OrderItem item) {
        int newStock = currentStock - item.getQuantity();
        if (newStock < 0) {
            throw new IllegalArgumentException("Error crítico: el stock resultante sería negativo para el producto ID: " + item.getId());
        }
        booksCatalogueFacade.updateBookStock(item.getBookId(), newStock);
    }
}
