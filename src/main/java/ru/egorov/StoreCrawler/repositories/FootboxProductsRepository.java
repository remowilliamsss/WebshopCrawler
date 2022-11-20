package ru.egorov.StoreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.StoreCrawler.models.FootboxProduct;
import ru.egorov.StoreCrawler.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface FootboxProductsRepository extends JpaRepository<FootboxProduct, Integer> {

    // TODO: 20.11.2022 sku - уникальное поле? Если нет, могут быть сюрпризы
    Optional<Product> findBySku(String sku);

    // TODO: 20.11.2022 Зачем Optional? Если ничего не найдет - вернет пустой List
    Optional<List<Product>> findAllByNameContainingIgnoreCase(String name);
}
