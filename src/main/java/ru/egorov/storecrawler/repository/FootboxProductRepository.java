package ru.egorov.storecrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.storecrawler.model.FootboxProduct;

@Repository
public interface FootboxProductRepository extends JpaRepository<FootboxProduct, Integer> {

}
