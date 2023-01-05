package ru.egorov.StoreCrawler.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.egorov.StoreCrawler.dto.ProductDto;
import ru.egorov.StoreCrawler.dto.SearchRequest;
import ru.egorov.StoreCrawler.dto.SearchResponse;
import ru.egorov.StoreCrawler.service.SearchService;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductsControllerTest {
    @Autowired
    private ProductsController productsController;
    @MockBean
    private SearchService searchService;

    @Test
    void search() {
        SearchRequest searchRequest = new SearchRequest("some query");

        ResponseEntity<SearchResponse> response = productsController.search(searchRequest);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .search(ArgumentMatchers.eq(searchRequest.getQuery()));
    }

    @Test
    void searchForNullQuery() {
        SearchRequest searchRequest = new SearchRequest(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ResponseEntity<SearchResponse> response = productsController.search(searchRequest);

            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        }, "ConstraintViolationException was expected");

        Mockito.verify(searchService, Mockito.times(0))
                .search(ArgumentMatchers.anyString());
    }

    @Test
    void searchForEmptyQuery() {
        SearchRequest searchRequest = new SearchRequest("");

        assertThrows(ConstraintViolationException.class, () -> {
            ResponseEntity<SearchResponse> response = productsController.search(searchRequest);

            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        }, "ConstraintViolationException was expected");

        Mockito.verify(searchService, Mockito.times(0))
                .search(ArgumentMatchers.anyString());
    }

    @Test
    void findBySku() {
        SearchRequest searchRequest = new SearchRequest("some sku");

        ResponseEntity<SearchResponse> response = productsController.findBySku(searchRequest);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .findBySku(ArgumentMatchers.eq(searchRequest.getQuery()));
    }

    @Test
    void findBySkuForNullQuery() {
        SearchRequest searchRequest = new SearchRequest(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ResponseEntity<SearchResponse> response = productsController.findBySku(searchRequest);

            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        }, "ConstraintViolationException was expected");

        Mockito.verify(searchService, Mockito.times(0))
                .findBySku(ArgumentMatchers.anyString());
    }

    @Test
    void findBySkuForEmptyQuery() {
        SearchRequest searchRequest = new SearchRequest("");

        assertThrows(ConstraintViolationException.class, () -> {
            ResponseEntity<SearchResponse> response = productsController.findBySku(searchRequest);

            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        }, "ConstraintViolationException was expected");

        Mockito.verify(searchService, Mockito.times(0))
                .findBySku(ArgumentMatchers.anyString());
    }

    @Test
    void findByStore() {
        String storeName = "sneakerhead";
        Pageable pageable = PageRequest.of(0, 20);

        ResponseEntity<List<ProductDto>> response = productsController.findByStore(storeName, pageable);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .findByStore(ArgumentMatchers.eq(storeName), ArgumentMatchers.eq(pageable));
    }

    @Test
    void findByStoreForNotSupportedStore() {
        String storeName = "some store";
        Pageable pageable = PageRequest.of(0, 20);

        assertThrows(ConstraintViolationException.class, () -> {
            ResponseEntity<List<ProductDto>> response = productsController.findByStore(storeName, pageable);

            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        }, "ConstraintViolationException was expected");

        Mockito.verify(searchService, Mockito.times(0))
                .findByStore(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class));
    }
}