package com.unir.orders.controller;

import com.unir.orders.controller.model.CreateOrderRequestDto;
import com.unir.orders.controller.model.CreateOrderResponseDto;
import com.unir.orders.controller.model.GetOrdersResponseDto;
import com.unir.orders.service.CreateOrdersService;
import com.unir.orders.service.GetOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final CreateOrdersService createOrdersService;
    private final GetOrdersService getOrdersService;

    @PostMapping("orders")
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(request));
    }

    @GetMapping("orders")
    public ResponseEntity<GetOrdersResponseDto> getRecentOrders() {
        return ResponseEntity.ok(getOrdersService.getRecentOrders());
    }
}
