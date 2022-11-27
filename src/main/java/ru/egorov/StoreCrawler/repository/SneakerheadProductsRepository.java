package ru.egorov.StoreCrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.model.SneakerheadProduct;

import java.util.List;


@Repository
public interface SneakerheadProductsRepository extends JpaRepository<SneakerheadProduct, Integer> {

    List<SneakerheadProduct> findAllByNameContainingIgnoreCase(String name);
}
