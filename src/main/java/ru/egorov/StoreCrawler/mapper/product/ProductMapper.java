package ru.egorov.StoreCrawler.mapper.product;

import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.model.Product;

public interface ProductMapper {

    ProductDto toDto(Product product);
}
