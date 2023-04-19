package ru.egorov.storecrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.storecrawler.model.SneakerheadProduct;

@Repository
public interface SneakerheadProductRepository extends JpaRepository<SneakerheadProduct, Integer> {
}
