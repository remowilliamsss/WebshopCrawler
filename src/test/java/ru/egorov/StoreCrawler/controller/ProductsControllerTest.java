package ru.egorov.StoreCrawler.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.egorov.StoreCrawler.SearchTest;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.dto.search.SearchRequest;
import ru.egorov.StoreCrawler.dto.search.SearchResultDto;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.SearchService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductsControllerTest {
    @Autowired
    private ProductsController productsController;
    @MockBean
    private SearchService searchService;
    @Mock
    private BindingResult bindingResult;

    @Test
    void search() {
        SearchRequest searchRequest = new SearchRequest(SearchTest.SOME_STRING);

        ResponseEntity<SearchResultDto> response = productsController.search(searchRequest, bindingResult);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .search(ArgumentMatchers.eq(searchRequest.getQuery()));
    }

    @Test
    void findBySku() {
        SearchRequest searchRequest = new SearchRequest(SearchTest.SOME_STRING);

        ResponseEntity<SearchResultDto> response = productsController.findBySku(searchRequest, bindingResult);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .findBySku(ArgumentMatchers.eq(searchRequest.getQuery()));
    }

    @Test
    void findByStore() {
        StoreType storeType = StoreType.sneakerhead;
        Pageable pageable = PageRequest.of(0, 20);

        ResponseEntity<List<ProductDto>> response = productsController.findByStore(storeType, pageable);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(searchService, Mockito.times(1))
                .findByStore(ArgumentMatchers.eq(storeType), ArgumentMatchers.eq(pageable));
    }
}