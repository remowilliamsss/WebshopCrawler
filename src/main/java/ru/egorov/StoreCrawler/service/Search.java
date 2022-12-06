package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.dto.SearchResponse;
import ru.egorov.StoreCrawler.dto.FoundProduct;
import ru.egorov.StoreCrawler.dto.ProductDifferences;
import ru.egorov.StoreCrawler.model.Product;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Search {
    private final List<ProductsService> productsServices;

    public SearchResponse search(String query) {
        if (query.isBlank()) {
            return new SearchResponse(Collections.emptyList());
        }

        List<FoundProduct> foundProductList = new ArrayList<>();

        productsServices.stream()
            .map(productsService -> productsService.findAllByName(query))
            .filter(products -> !products.isEmpty())
            .forEach(products -> {
                products.forEach(product -> {
                    if (foundProductList.stream()
                            .noneMatch(foundProduct -> foundProduct.getSku().equals(product.getSku()))) {

                        foundProductList.add(createFoundProduct(product));
                    } else {
                        foundProductList.stream()
                                .filter(foundProduct -> foundProduct.getSku().equals(product.getSku()))
                                .findFirst()
                                .get()
                                .getDifferences()
                                .add(createProductDifferences(product));
                    }
                });
            });

        foundProductList.forEach(foundProduct -> Collections.sort(foundProduct.getDifferences()));
        Collections.sort(foundProductList);

        return new SearchResponse(foundProductList);
    }

    public SearchResponse findBySku(String sku) {
        if (sku.isBlank()) {
            return new SearchResponse(Collections.emptyList());
        }

        List<FoundProduct> foundProductList = new ArrayList<>(1);

        productsServices.stream()
                .map(productsService -> productsService.findBySku(sku))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(product -> {
                    if (foundProductList.isEmpty()) {
                        foundProductList.add(createFoundProduct(product));
                    } else {
                        foundProductList.get(0)
                                .getDifferences()
                                .add(createProductDifferences(product));
                    }
                });

        Collections.sort(foundProductList.get(0).getDifferences());

        return new SearchResponse(foundProductList);
    }

    private FoundProduct createFoundProduct(Product product) {
        List<ProductDifferences> differences = new ArrayList<>();

        differences.add(createProductDifferences(product));

        return new FoundProduct(
                product.getName(), product.getSku(),
                product.getImage(), product.getCategory(),
                product.getBrand(), product.getColor(),
                product.getCountry(), product.getGender(),
                differences);
    }

    private ProductDifferences createProductDifferences(Product product) {
        return new ProductDifferences(
                product.getStore().getName(),
                product.getPrice(), product.getPriceCurrency(),
                product.getSizes(), product.getUrl());
    }
}
