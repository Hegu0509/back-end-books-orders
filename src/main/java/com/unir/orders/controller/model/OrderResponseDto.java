package com.unir.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "userId", "status", "totalAmount", "currency", "createdAt", "items"})
@Getter
@Builder
public class OrderResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    @JsonProperty("items")
    private List<OrderItemResponseDto> items;
}
