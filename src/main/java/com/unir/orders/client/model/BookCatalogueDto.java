package com.unir.orders.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookCatalogueDto {

    private Long id;
    private String title;
    private String isbn;
    private Integer stock;
    private BigDecimal price;
    private Boolean visible;
}
