package ru.egorov.storecrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.storecrawler.dto.product.ProductDto;
import ru.egorov.storecrawler.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);
}
