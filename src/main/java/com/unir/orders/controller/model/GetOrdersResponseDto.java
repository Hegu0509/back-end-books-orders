package com.unir.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class GetOrdersResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("orders")
    private List<OrderResponseDto> orders;
}
