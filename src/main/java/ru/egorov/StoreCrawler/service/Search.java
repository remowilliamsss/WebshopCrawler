package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.dto.SearchResponse;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Search {
    private final List<ProductsService> productsServices;

    public List<SearchResponse> search(String query) {
        List<SearchResponse> resultList = new ArrayList<>();

        productsServices.stream()
            .map(productsService -> productsService.findAllByName(query))
            .filter(products -> !products.isEmpty())
            .forEach(products -> {
                products.forEach(product -> {
                    if (resultList.stream()
                            .noneMatch(searchResponse -> searchResponse.getSku().equals(product.getSku()))) {
                        HashMap<String, Double> priceList = new HashMap<>(
                                Map.of(product.getStore().getName(), product.getPrice()));
                        resultList.add(new SearchResponse(product.getSku(), product.getName(), priceList));
                    } else {
                        resultList.stream()
                                .filter(searchResponse -> searchResponse.getSku().equals(product.getSku()))
                                .findFirst()
                                .get()
                                .getPriceList()
                                .put(product.getStore().getName(), product.getPrice());
                    }
                });
            });

        Collections.sort(resultList);

        return resultList;
    }
}
