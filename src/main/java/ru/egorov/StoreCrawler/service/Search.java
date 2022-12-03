package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.dto.SearchResponse;
import ru.egorov.StoreCrawler.dto.FoundProduct;
import ru.egorov.StoreCrawler.dto.ProductDifferences;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Search {
    private final List<ProductsService> productsServices;

    public SearchResponse search(String query) {
        List<FoundProduct> foundProductList = new ArrayList<>();

        productsServices.stream()
            .map(productsService -> productsService.findAllByName(query))
            .filter(products -> !products.isEmpty())
            .forEach(products -> {
                products.forEach(product -> {
                    if (foundProductList.stream()
                            .noneMatch(foundProduct -> foundProduct.getSku().equals(product.getSku()))) {

                        List<ProductDifferences> differences = new ArrayList<>();
                        differences.add(new ProductDifferences(
                                product.getStore().getName(),
                                product.getPrice(), product.getPriceCurrency(),
                                product.getSizes(), product.getUrl()));

                        foundProductList.add(new FoundProduct(
                                product.getName(), product.getSku(),
                                product.getImage(), product.getCategory(),
                                product.getBrand(), product.getColor(),
                                product.getCountry(), product.getGender(),
                                differences));
                    } else {
                        foundProductList.stream()
                                .filter(foundProduct -> foundProduct.getSku().equals(product.getSku()))
                                .findFirst()
                                .get()
                                .getDifferences()
                                .add(new ProductDifferences(
                                        product.getStore().getName(),
                                        product.getPrice(), product.getPriceCurrency(),
                                        product.getSizes(), product.getUrl()));
                    }
                });
            });

        foundProductList.forEach(foundProduct -> Collections.sort(foundProduct.getDifferences()));
        Collections.sort(foundProductList);

        return new SearchResponse(foundProductList);
    }
}
