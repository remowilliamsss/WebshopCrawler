package ru.egorov.StoreCrawler.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.Dispatcher;
import ru.egorov.StoreCrawler.dto.ProductDto;
import ru.egorov.StoreCrawler.dto.SearchRequest;
import ru.egorov.StoreCrawler.dto.SearchResponse;
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
    // TODO: 13.12.2022 lombok, @Slf4j
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    // TODO: 13.12.2022 Сервис. SearchService, DispatcherService и т.д.
    //  Если твой класс процессит бизнес-логику, он должен быть сервисом.
    //  И аннотация внутри дб соответствующей
    private final Search search;
    private final Dispatcher dispatcher;
    private final Crawler crawler;

    @PostMapping("/search")
    public ResponseEntity<SearchResponse> search(@Valid @RequestBody SearchRequest request) {
        String query = request.getQuery();
// TODO: 13.12.2022 логи в контроллере - это извращение
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
                                @RequestParam(value = "page", required = false) Integer page,// TODO: 13.12.2022 Pageable
                                @RequestParam(value = "productsPerPage", required = false) Integer productsPerPage) {
        // TODO: 13.12.2022 контроллер должен только вызывать сервис.
        //  Максимум - вызывать мапперы и сервис. У тебя он явно перегружен
        // TODO: 13.12.2022 если тернарка не влазит в одну строку, лучше переноси так:
        //  condition
        //      ? logic
        //      : logic

        // TODO: 13.12.2022 Если уже и писать подобное, то лучше так:
//        var productsService = dispatcher.getService(storeName);
//        var products = page == null || productsPerPage == null
//                ? productsService.findAll()
//                : productsService.findAll(page, productsPerPage);
//
//        var mapper = dispatcher.getMapper(storeName);
//        var productDtos = products.stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(productDtos);

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
