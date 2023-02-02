package ru.egorov.StoreCrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.model.FootboxProduct;

import java.util.List;

@Repository
public interface FootboxProductRepository extends JpaRepository<FootboxProduct, Integer> {

    List<FootboxProduct> findAllByNameContainingIgnoreCase(String name);

    FootboxProduct findBySku(String sku);
}
