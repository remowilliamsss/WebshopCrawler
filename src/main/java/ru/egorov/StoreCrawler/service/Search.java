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

    public FoundProduct findBySku(String sku) {
        FoundProduct foundProduct = new FoundProduct();

        productsServices.forEach(productsService -> {
            Optional<? extends Product> optionalProduct = productsService.findBySku(sku);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                if (foundProduct.getSku() == null) {
                    enrichFoundProduct(foundProduct, product);
                } else {
                    foundProduct.getDifferences()
                            .add(createProductDifferences(product));
                }
            }
        });

        Collections.sort(foundProduct.getDifferences());

        return foundProduct;
    }

    private void enrichFoundProduct(FoundProduct foundProduct, Product product) {
            foundProduct.setName(product.getName());
            foundProduct.setSku(product.getSku());
            foundProduct.setImage(product.getImage());
            foundProduct.setCategory(product.getCategory());
            foundProduct.setBrand(product.getBrand());
            foundProduct.setColor(product.getColor());
            foundProduct.setCountry(product.getCountry());
            foundProduct.setGender(foundProduct.getGender());

            List<ProductDifferences> differences = new ArrayList<>();
            differences.add(createProductDifferences(product));
            foundProduct.setDifferences(differences);
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
