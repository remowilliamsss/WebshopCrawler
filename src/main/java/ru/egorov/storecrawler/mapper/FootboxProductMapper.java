package ru.egorov.storecrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.storecrawler.dto.product.FootboxProductDto;
import ru.egorov.storecrawler.model.FootboxProduct;
import ru.egorov.storecrawler.model.Product;

@Mapper(componentModel = "spring")
public interface FootboxProductMapper extends ProductMapper {

    @Override
    default FootboxProductDto toDto(Product product) {
        return toDto((FootboxProduct) product);
    }

    FootboxProductDto toDto(FootboxProduct product);
}
