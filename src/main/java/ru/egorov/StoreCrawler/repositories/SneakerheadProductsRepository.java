package ru.egorov.StoreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;

import java.util.List;
import java.util.Optional;


@Repository
public interface SneakerheadProductsRepository extends JpaRepository<SneakerheadProduct, Integer> {

    Optional<Product> findBySku(String sku);

    Optional<List<Product>> findAllByNameContainingIgnoreCase(String name);
}
