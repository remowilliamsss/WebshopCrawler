package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.dto.product.FootboxProductDto;
import ru.egorov.StoreCrawler.model.product.FootboxProduct;
import ru.egorov.StoreCrawler.model.product.Product;

@Mapper(componentModel = "spring")
public interface FootboxProductMapper extends ProductMapper {

    @Override
    default FootboxProductDto toDto(Product product) {
        return toDto((FootboxProduct) product);
    }

    FootboxProductDto toDto(FootboxProduct product);
}
