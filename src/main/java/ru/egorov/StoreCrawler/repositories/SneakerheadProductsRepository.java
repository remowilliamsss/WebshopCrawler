package ru.egorov.StoreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;

import java.util.Optional;


@Repository
public interface SneakerheadProductsRepository extends JpaRepository<SneakerheadProduct, Integer> {

    Optional<SneakerheadProduct> findBySku(String sku);
}
