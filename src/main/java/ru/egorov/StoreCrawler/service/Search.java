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
// TODO: 13.12.2022 уже писал, но: некорректное название и аннотация над классом
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
            .forEach(products -> {// TODO: 13.12.2022 избыточные {}
                // TODO: 13.12.2022 познакомься с flatMap() :)
                products.forEach(product -> {
                    // TODO: 13.12.2022 условие ифа вынеси в переменную
                    if (foundProductList.stream()
                            .noneMatch(foundProduct -> foundProduct.getSku().equals(product.getSku()))) {
                        // TODO: 13.12.2022 кажется, огород с id-else здесь лишний, можно попробовать без него
                        foundProductList.add(createFoundProduct(product));
                    } else {
                        foundProductList.stream()
                                .filter(foundProduct -> foundProduct.getSku().equals(product.getSku()))
                                .findFirst()
                                // TODO: 13.12.2022 вызов get()
                                .get()
                                .getDifferences()
                                .add(createProductDifferences(product));
                    }
                });
            });

        foundProductList.forEach(foundProduct -> Collections.sort(foundProduct.getDifferences()));
        Collections.sort(foundProductList);

        // TODO: 13.12.2022 зачем в этой схеме SearchResponse?
        return new SearchResponse(foundProductList);
    }

    public SearchResponse findBySku(String sku) {
        if (sku.isBlank()) {
            return new SearchResponse(Collections.emptyList());
        }

        List<FoundProduct> foundProductList = new ArrayList<>(1);

        productsServices.stream()
                //findAllBySkuIn для репы. Сейчас куча лишних запросов в базу
                .map(productsService -> productsService.findBySku(sku))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(product -> {
                    // TODO: 13.12.2022 кажется, огород с id-else здесь лишний, можно попробовать без него
                    if (foundProductList.isEmpty()) {
                        foundProductList.add(createFoundProduct(product));
                    } else {
                        foundProductList.get(0)
                                .getDifferences()
                                .add(createProductDifferences(product));
                    }
                });

        // TODO: 13.12.2022 если пришлось написать get(0) - ты почти гарантированно ошибся
        Collections.sort(foundProductList.get(0).getDifferences());

        return new SearchResponse(foundProductList);
    }

    // TODO: 13.12.2022 выглядит как логика для маппера
    private FoundProduct createFoundProduct(Product product) {
        List<ProductDifferences> differences = new ArrayList<>();

        differences.add(createProductDifferences(product));

        // TODO: 13.12.2022 @Builder в Lombok
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
