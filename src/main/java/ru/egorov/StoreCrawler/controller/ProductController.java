package ru.egorov.StoreCrawler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.ProductResponse;
import ru.egorov.StoreCrawler.model.product.StoreType;
import ru.egorov.StoreCrawler.service.ProductProviderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    public static final String STORE = "store";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    private final ProductProviderService productProviderService;

    @GetMapping
    public ResponseEntity<ProductResponse> getMany(@RequestParam(name = STORE) StoreType store,
                                                   @RequestParam(name = PAGE, required = false) Integer page,
                                                   @RequestParam(name = SIZE, required = false) Integer size) {

        ProductResponse response = (page == null || size == null) ?
                productProviderService.gain(store) : productProviderService.gain(store, PageRequest.of(page, size));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
