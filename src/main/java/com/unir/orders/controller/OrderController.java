package com.unir.orders.controller;

import com.unir.orders.controller.model.CreateOrderRequestDto;
import com.unir.orders.controller.model.GetOrdersResponseDto;
import com.unir.orders.controller.model.OrderResponseDto;
import com.unir.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        OrderResponseDto created = orderService.createOrder(request);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + created.getId()))
                .body(created);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetOrdersResponseDto> getOrdersByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}
