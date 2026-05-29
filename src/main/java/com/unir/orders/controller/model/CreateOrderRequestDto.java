package com.unir.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDto {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("items")
    private List<OrderItemRequestDto> items;
}
