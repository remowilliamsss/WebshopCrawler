package ru.egorov.StoreCrawler.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.model.product.SneakerheadProduct;

@Repository
public interface SneakerheadProductRepository extends JpaRepository<SneakerheadProduct, Integer> {
}
