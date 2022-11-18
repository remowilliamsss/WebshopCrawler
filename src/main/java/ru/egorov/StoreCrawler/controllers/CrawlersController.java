package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.models.FootboxProduct;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;
import ru.egorov.StoreCrawler.services.FootboxProductsService;
import ru.egorov.StoreCrawler.services.SneakerheadProductsService;
import ru.egorov.StoreCrawler.util.SearchRequest;
import ru.egorov.StoreCrawler.util.SearchResult;
import ru.egorov.StoreCrawler.util.SearchResultResponse;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("crawlers")
public class CrawlersController {
    private final SneakerheadProductsService sneakerheadProductsService;
    private final FootboxProductsService footboxProductsService;

    @Autowired
    public CrawlersController(SneakerheadProductsService sneakerheadProductsService,
                              FootboxProductsService footboxProductsService) {
        this.sneakerheadProductsService = sneakerheadProductsService;
        this.footboxProductsService = footboxProductsService;
    }

    @PostMapping("/search")
    public SearchResultResponse search(@RequestBody SearchRequest request) {
        if (request.getQuery() == null)
            return null;

        SearchResultResponse response = new SearchResultResponse();
        List<SearchResult> resultList = new ArrayList<>();

        List<FootboxProduct> foundFootboxProducts = footboxProductsService.findAllByName(request.getQuery());

        if (!foundFootboxProducts.isEmpty()) {
            for (FootboxProduct footboxProduct : foundFootboxProducts) {
                SearchResult searchResult = new SearchResult();

                searchResult.setItemName(footboxProduct.getName());

                Map<String, Double> priceList = new HashMap<>();

                priceList.put(footboxProduct.getClass().getName(), footboxProduct.getPrice());

                Optional<SneakerheadProduct> foundSneakerheadProduct = sneakerheadProductsService.findBySku(
                        footboxProduct.getSku());

                if (foundSneakerheadProduct.isPresent()) {
                    SneakerheadProduct sneakerheadProduct = foundSneakerheadProduct.get();
                    priceList.put(sneakerheadProduct.getClass().getName(), sneakerheadProduct.getPrice());
                }
                searchResult.setPriceList(priceList);

                resultList.add(searchResult);
            }
        } else {
            List<SneakerheadProduct> foundSneakerheadProducts = sneakerheadProductsService.findAllByName(request.getQuery());

            if (!foundSneakerheadProducts.isEmpty()) {
                for (SneakerheadProduct sneakerheadProduct : foundSneakerheadProducts) {
                    SearchResult searchResult = new SearchResult();

                    searchResult.setItemName(sneakerheadProduct.getName());

                    Map<String, Double> priceList = new HashMap<>();

                    priceList.put(sneakerheadProduct.getClass().getName(), sneakerheadProduct.getPrice());

                    searchResult.setPriceList(priceList);

                    resultList.add(searchResult);
                }
            }
        }
        response.setResultList(resultList);

        return response;
    }

    @GetMapping("/test")
    public String test() throws IOException {
        return "test";
    }
}
