package com.unir.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDto {

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("quantity")
    private Integer quantity;
}
