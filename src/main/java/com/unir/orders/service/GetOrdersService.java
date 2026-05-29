package com.unir.orders.service;

import com.unir.orders.controller.model.GetOrdersResponseDto;
import com.unir.orders.controller.model.PurchasedItem;
import com.unir.orders.controller.model.RecentOrder;
import com.unir.orders.facade.BooksCatalogueFacade;
import com.unir.orders.facade.model.BookDto;
import com.unir.orders.repository.OrderJpaRepository;
import com.unir.orders.repository.model.Order;
import com.unir.orders.repository.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersService {

    private final OrderJpaRepository orderJpaRepository;
    private final BooksCatalogueFacade booksCatalogueFacade;

    @Transactional(readOnly = true)
    public GetOrdersResponseDto getRecentOrders() {
        List<Order> recentOrders = orderJpaRepository.findByUserIdOrderByCreatedAtDesc("1", Limit.of(5)); // Se debera obtener de contexto de seguridad
        return GetOrdersResponseDto.builder()
                .recentOrders(recentOrders.stream().map(this::getRecentOrder).toList())
                .build();
    }

    private RecentOrder getRecentOrder(Order order) {
        List<OrderItem> orderItems = order.getItems();
        List<PurchasedItem> purchasedItems = orderItems.stream().map(this::getBookData).toList();
        return RecentOrder.builder()
                .id(order.getId().toString())
                .status(order.getStatus())
                .total(order.getTotalAmount().doubleValue())
                .date(order.getCreatedAt().toLocalDate().toString())
                .items(purchasedItems)
                .build();
    }

    private PurchasedItem getBookData(OrderItem orderItem) {
        BookDto book = booksCatalogueFacade.getBook(orderItem.getBookId());
        return PurchasedItem.builder()
                .name(book.getTitle())
                .price(book.getPrice().doubleValue())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
