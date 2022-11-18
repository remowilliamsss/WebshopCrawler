package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.services.ProductsService;
import ru.egorov.StoreCrawler.util.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("crawlers")
public class CrawlersController {
    private final List<ProductsService> productsServices;

    @Autowired
    public CrawlersController(ProductsService... productsServices) {
        this.productsServices = new ArrayList<>(List.of(productsServices));
    }

    @PostMapping("/search")
    public SearchResultResponse search(@RequestBody SearchRequest request) {
        if (request.getQuery() == null)
            throw new NullQueryException();

        SearchResultResponse response = new SearchResultResponse();
        List<SearchResult> resultList = new ArrayList<>();

        for (ProductsService productsService : productsServices) {
            List<Product> foundProducts = productsService.findAllByName(request.getQuery());

            if (!foundProducts.isEmpty()) {
                String name = foundProducts.get(0).getClass().getName();
                name = name.substring(name.lastIndexOf('.') + 1, name.length() - 7);

                for (Product product : foundProducts) {
                    if (resultList.stream().noneMatch(searchResult -> searchResult.getSku().equals(product.getSku()))) {
                        SearchResult searchResult = new SearchResult();
                        searchResult.setSku(product.getSku());
                        searchResult.setItemName(product.getName());

                        Map<String, Double> priceList = new HashMap<>();
                        priceList.put(name, product.getPrice());
                        searchResult.setPriceList(priceList);

                        resultList.add(searchResult);
                    } else {
                        SearchResult searchResult = resultList.stream().filter(
                                sr -> sr.getSku().equals(product.getSku())).findFirst().get();

                        searchResult.getPriceList().put(name, product.getPrice());
                    }
                }
            }
        }
        if (resultList.isEmpty())
            throw new NothingFoundException();

        Collections.sort(resultList);

        response.setResultList(resultList);

        return response;
    }

    @GetMapping("/test")
    public String test() throws IOException {
        return "test";
    }

    @ExceptionHandler
    private ResponseEntity<SearchErrorResponse> handleException(NullQueryException e) {
        SearchErrorResponse response = new SearchErrorResponse("Query is null", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<SearchErrorResponse> handleException(NothingFoundException e) {
        SearchErrorResponse response = new SearchErrorResponse("Nothing found", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
