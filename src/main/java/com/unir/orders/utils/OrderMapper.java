package com.unir.orders.utils;

import com.unir.orders.controller.model.GetOrdersResponseDto;
import com.unir.orders.controller.model.OrderItemResponseDto;
import com.unir.orders.controller.model.OrderResponseDto;
import com.unir.orders.repository.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDto asOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(item -> OrderItemResponseDto.builder()
                                .bookId(item.getBookId())
                                .bookTitle(item.getBookTitle())
                                .isbn(item.getIsbn())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .toList())
                .build();
    }

    public GetOrdersResponseDto asGetOrdersResponseDto(List<Order> orders) {
        return GetOrdersResponseDto.builder()
                .orders(orders.stream().map(this::asOrderResponseDto).toList())
                .build();
    }
}
