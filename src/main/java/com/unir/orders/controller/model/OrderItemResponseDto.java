package com.unir.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"bookId", "bookTitle", "isbn", "quantity", "unitPrice", "subtotal"})
@Getter
@Builder
public class OrderItemResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bookId")
    private Long bookId;
    @JsonProperty("bookTitle")
    private String bookTitle;
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;
    @JsonProperty("subtotal")
    private BigDecimal subtotal;
}
