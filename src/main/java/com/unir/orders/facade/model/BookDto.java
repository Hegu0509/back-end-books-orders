package com.unir.orders.facade.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookDto {

    @JsonProperty("id")
    public Long id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("description")
    public String description;
    @JsonProperty("isbn")
    public String isbn;
    @JsonProperty("valoracion")
    public BigDecimal valoracion;
    @JsonProperty("stock")
    public Integer stock;
    @JsonProperty("price")
    public BigDecimal price;
    @JsonProperty("author")
    public String author;
    @JsonProperty("publisher")
    public String publisher;
    @JsonProperty("category")
    public String category;
    @JsonProperty("images")
    public List<String> images;
}
