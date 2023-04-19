package ru.egorov.storecrawler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Товары", description = "Методы для работы с товарами")
public class ProductController {

    private final ProductProviderService productProviderService;

    @GetMapping
    @Operation(summary = "Получение всех товаров магазина")
    public ResponseEntity<Page<ProductDto>> getMany(
            @RequestParam(name = "store") @Parameter(description = "Название магазина") StoreType store,
            @PageableDefault(size = 30) @ParameterObject Pageable pageable) {

        Page<ProductDto> response = productProviderService.gain(store, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
