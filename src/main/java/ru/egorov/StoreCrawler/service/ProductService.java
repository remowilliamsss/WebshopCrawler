package ru.egorov.StoreCrawler.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.egorov.StoreCrawler.model.Product;

import java.util.List;

public interface ProductService {
    Page<? extends Product> findAll(Pageable pageable);

    List<? extends Product> findAllByName(String name);

    List<? extends Product> updateAll(List<Product> products);

    Product findBySku(String sku);
}
