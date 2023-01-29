package ru.egorov.StoreCrawler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.dto.search.SearchRequest;
import ru.egorov.StoreCrawler.dto.search.SearchResultDto;
import ru.egorov.StoreCrawler.exception.BadQueryException;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.SearchService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductsController {
    private final SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<SearchResultDto> search(@Valid @RequestBody SearchRequest request,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadQueryException(getErrorMessage(bindingResult));
        }

        String query = request.getQuery();

        SearchResultDto searchResultDto = searchService.search(query);

        return new ResponseEntity<>(searchResultDto, HttpStatus.OK);
    }

    @PostMapping("/find_by_sku")
    public ResponseEntity<SearchResultDto> findBySku(@Valid @RequestBody SearchRequest request,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadQueryException(getErrorMessage(bindingResult));
        }

        String query = request.getQuery();

        SearchResultDto searchResultDto = searchService.findBySku(query);

        return new ResponseEntity<>(searchResultDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findByStore(@RequestParam(name="store") StoreType store,
                                                        Pageable pageable) {
        List<ProductDto> productDtos = searchService.findByStore(store, pageable);

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    private String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        bindingResult.getFieldErrors()
                .forEach(error -> errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";")
                );

        return errorMessage.toString();
    }
}
