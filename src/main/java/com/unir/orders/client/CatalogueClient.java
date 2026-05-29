package com.unir.orders.client;

import com.unir.orders.client.model.BookCatalogueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "books-catalogue")
public interface CatalogueClient {

    @GetMapping("/api/v1/books/{bookId}")
    BookCatalogueDto getBook(@PathVariable("bookId") Long bookId);
}
