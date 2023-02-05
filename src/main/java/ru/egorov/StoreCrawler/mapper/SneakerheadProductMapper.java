package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.dto.product.SneakerheadProductDto;
import ru.egorov.StoreCrawler.model.Product;

@Mapper(componentModel = "spring")
public interface SneakerheadProductMapper extends ProductMapper {

    SneakerheadProductDto toDto(Product product);
}
