package ru.egorov.StoreCrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.model.FootboxProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface FootboxProductsRepository extends JpaRepository<FootboxProduct, Integer> {

    List<FootboxProduct> findAllByNameContainingIgnoreCase(String name);

    Optional<FootboxProduct> findBySku(String sku);
}
