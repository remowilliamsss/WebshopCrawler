package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.dto.FootboxProductDto;
import ru.egorov.StoreCrawler.model.FootboxProduct;
import ru.egorov.StoreCrawler.model.Product;

@Mapper(componentModel = "spring")
public interface FootboxProductMapper extends ProductMapper{

    @Override
    default FootboxProductDto toDto(Product product) {
        return toDto((FootboxProduct) product);
    }

    FootboxProductDto toDto(FootboxProduct product);

    @Override
    default Store getStore() {
        return Store.FOOTBOX;
    }
}
