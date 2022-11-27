package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.dto.SneakerheadProductDto;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.SneakerheadProduct;

@Mapper(componentModel = "spring")
public interface SneakerheadProductMapper extends ProductMapper {

    @Override
    default SneakerheadProductDto toDto(Product product) {
        return toDto((SneakerheadProduct) product);
    }

    SneakerheadProductDto toDto(SneakerheadProduct product);

    @Override
    default Store getStore() {
        return Store.SNEAKERHEAD;
    }
}
