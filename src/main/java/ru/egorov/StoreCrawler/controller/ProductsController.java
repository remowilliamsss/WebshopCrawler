package ru.egorov.StoreCrawler.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.Dispatcher;
import ru.egorov.StoreCrawler.dto.*;
import ru.egorov.StoreCrawler.service.Crawler;
import ru.egorov.StoreCrawler.service.Search;
import ru.egorov.StoreCrawler.validator.StoreName;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Validated
public class ProductsController {
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    private final Search search;
    private final Dispatcher dispatcher;
    private final Crawler crawler;

    @PostMapping("/search")
    public ResponseEntity<SearchResponse> search(@Valid @RequestBody SearchRequest request) {
        String query = request.getQuery();

        log.info("Search for \"{}\" starts", query);

        SearchResponse searchResponse = search.search(query);

        log.info("Search for \"{}\" finished with {} results", query, searchResponse.getFoundProductList().size());

        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @PostMapping("/find_by_sku")
    public ResponseEntity<SearchResponse> findBySku(@Valid @RequestBody SearchRequest request) {
        String query = request.getQuery();

        log.info("Search for \"{}\" starts", query);

        SearchResponse searchResponse = search.findBySku(query);

        log.info("Search for \"{}\" finished", query);

        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @GetMapping("/{storeName}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable("storeName") @StoreName String storeName,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "productsPerPage", required = false) Integer productsPerPage) {
        List<ProductDto> response = (page == null || productsPerPage == null) ?
                dispatcher.getService(storeName)
                        .findAll()
                        .stream()
                        .map(product -> dispatcher.getMapper(storeName)
                                .toDto(product))
                        .collect(Collectors.toList()) :
                dispatcher.getService(storeName)
                        .findAll(page, productsPerPage)
                        .stream()
                        .map(product -> dispatcher.getMapper(storeName)
                                .toDto(product))
                        .collect(Collectors.toList());

        log.info("Products from {} have been sent", storeName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test() {
        log.debug("Testing starts");

        crawler.crawl();

        log.debug("Testing finishes");
    }
}
