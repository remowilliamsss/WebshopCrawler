package ru.egorov.StoreCrawler.services;

import ru.egorov.StoreCrawler.models.Product;

import java.util.List;
import java.util.Optional;

public abstract class ProductsService {

    public abstract Optional<Product> findBySku(String sku);

    public abstract List<Product> findAllByName(String name);
}
