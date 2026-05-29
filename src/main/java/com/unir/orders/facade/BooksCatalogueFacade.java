package com.unir.orders.facade;

import com.unir.orders.exception.BadSupplyModificationException;
import com.unir.orders.exception.BookNotFoundException;
import com.unir.orders.exception.InternalErrorException;
import com.unir.orders.facade.model.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BooksCatalogueFacade {

    private final WebClient.Builder webClientBuilder;
    @Value("${booksCatalogue.url}")
    private String booksCatalogueUrl;

    public BookDto getBook(Long bookId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(booksCatalogueUrl + "/books/{id}", bookId)
                    .retrieve()
                    .bodyToMono(BookDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found", e);
        } catch (WebClientResponseException.InternalServerError e) {
            throw new InternalErrorException("An exception occurred fetching book with ID " + bookId, e);
        }
    }

    public void updateBookStock(Long bookId, Integer stock) {
        try {
            webClientBuilder.build().patch()
                    .uri(booksCatalogueUrl + "/books/{id}", bookId)
                    .bodyValue(Map.of("stock", stock))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found", e);
        } catch (WebClientResponseException.BadRequest e) {
            throw new BadSupplyModificationException("Bad request when updating stock for book with ID " + bookId, e);
        } catch (WebClientResponseException.InternalServerError e) {
            throw new InternalErrorException("An exception occurred fetching book with ID " + bookId, e);
        }
    }

}
