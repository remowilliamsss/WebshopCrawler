package ru.egorov.StoreCrawler.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.egorov.StoreCrawler.model.product.Product;

import java.util.List;

public interface ProductService {

    List<? extends Product> findAll();

    Page<? extends Product> findAll(Pageable pageable);

    List<? extends Product> updateAll(List<Product> products);
}
