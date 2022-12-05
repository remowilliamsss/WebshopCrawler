package ru.egorov.StoreCrawler.service;

import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.model.Product;

import java.util.List;
import java.util.Optional;

public abstract class ProductsService {
    public abstract List<? extends Product> findAll();

    public abstract List<? extends Product> findAll(Integer page, Integer productsPerPage);

    public abstract List<? extends Product> findAllByName(String name);

    public abstract List<? extends Product> updateProducts(List<Product> products);

    public abstract Optional<? extends Product> findBySku(String sku);

    public abstract Store getStore();
}
