package ru.egorov.storecrawler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.storecrawler.dto.product.ProductDto;
import ru.egorov.storecrawler.model.StoreType;
import ru.egorov.storecrawler.service.ProductProviderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

    private final ProductProviderService productProviderService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getMany(@RequestParam(name = "store") StoreType store,
                                    @PageableDefault(size = 30) Pageable pageable) {

        Page<ProductDto> response = productProviderService.gain(store, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
