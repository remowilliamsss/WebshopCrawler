package ru.egorov.StoreCrawler.mapper;

import org.mapstruct.Mapper;
import ru.egorov.StoreCrawler.dto.search.FoundProductDifference;
import ru.egorov.StoreCrawler.dto.search.FoundProductDto;
import ru.egorov.StoreCrawler.model.Product;

@Mapper(componentModel = "spring")
public interface FoundProductMapper {

    FoundProductDto toDto (Product product);

    FoundProductDifference extractDifference (Product product);
}
