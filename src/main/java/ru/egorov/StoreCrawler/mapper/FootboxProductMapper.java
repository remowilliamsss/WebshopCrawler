package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.dto.FootboxProductDto;
import ru.egorov.StoreCrawler.model.FootboxProduct;
import ru.egorov.StoreCrawler.model.Product;

@Mapper(componentModel = "spring")
public interface FootboxProductMapper extends ProductMapper{

    @Override
    // TODO: 13.12.2022 что-то ты намудрил тут, кмк. Стоит попробовать описать это через аннотации.
    //  Странно, если оно по дефолту не подхватилось
    default FootboxProductDto toDto(Product product) {
        return toDto((FootboxProduct) product);
    }

    FootboxProductDto toDto(FootboxProduct product);

    @Override
    // TODO: 13.12.2022 Что этот метод делает в эом маппере?)
    default Store getStore() {
        return Store.FOOTBOX;
    }
}
