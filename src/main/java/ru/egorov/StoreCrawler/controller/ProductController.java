package ru.egorov.StoreCrawler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.dto.search.FoundProductDto;
import ru.egorov.StoreCrawler.dto.search.SearchRequest;
import ru.egorov.StoreCrawler.dto.search.SearchResultDto;
import ru.egorov.StoreCrawler.exception.BadQueryException;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.SearchService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    private final SearchService searchService;

    public static final String STORE = "store";

    @PostMapping("/search")
    public ResponseEntity<List<FoundProductDto>> search(@Valid @RequestBody SearchRequest request,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadQueryException(getErrorMessage(bindingResult));
        }

        String query = request.getQuery();

        List<FoundProductDto> foundProductDtos = searchService.search(query);

        return new ResponseEntity<>(foundProductDtos, HttpStatus.OK);
    }

    @PostMapping("/find_by_sku")
    public ResponseEntity<List<FoundProductDto>> findBySku(@Valid @RequestBody SearchRequest request,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadQueryException(getErrorMessage(bindingResult));
        }

        String query = request.getQuery();

        List<FoundProductDto> foundProductDtos = searchService.findBySku(query);

        return new ResponseEntity<>(foundProductDtos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findByStore(@RequestParam(name=STORE) StoreType store, Pageable pageable) {
        List<ProductDto> productDtos = searchService.findByStore(store, pageable);

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(error -> String.format("%s - %s;", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
    }
}
