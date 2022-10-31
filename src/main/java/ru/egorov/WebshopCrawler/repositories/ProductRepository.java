package ru.egorov.WebshopCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.WebshopCrawler.models.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
