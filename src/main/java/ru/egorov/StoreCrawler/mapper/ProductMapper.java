package ru.egorov.StoreCrawler.mapper;

import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.model.Product;

public interface ProductMapper {

    ProductDto toDto(Product product);

    StoreType getStore();
}
