package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.dto.product.FootboxProductDto;
import ru.egorov.StoreCrawler.model.Product;

@Mapper(componentModel = "spring")
public interface FootboxProductMapper extends ProductMapper{

    FootboxProductDto toDto(Product product);
}
