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
import ru.egorov.StoreCrawler.dto.ProductResponse;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.ProductProviderService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductControllerTest {
    @Autowired
    private ProductController productController;
    @MockBean
    private ProductProviderService productProviderService;

    @Test
    void getMany() {
        StoreType storeType = StoreType.sneakerhead;

        ResponseEntity<ProductResponse> response = productController.getMany(storeType, null, null);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(productProviderService, Mockito.times(1))
                .gain(ArgumentMatchers.eq(storeType));

        Mockito.verify(productProviderService, Mockito.times(0))
                .gain(ArgumentMatchers.any(StoreType.class), ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void getManyWithPagination() {
        StoreType storeType = StoreType.sneakerhead;
        int page = 0;
        int size = 20;

        ResponseEntity<ProductResponse> response = productController.getMany(storeType, page, size);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Mockito.verify(productProviderService, Mockito.times(1))
                .gain(ArgumentMatchers.eq(storeType), ArgumentMatchers.eq(PageRequest.of(page, size)));

        Mockito.verify(productProviderService, Mockito.times(0))
                .gain(ArgumentMatchers.any(StoreType.class));
    }
}