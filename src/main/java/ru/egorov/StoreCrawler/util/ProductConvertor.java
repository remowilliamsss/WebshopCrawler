package ru.egorov.StoreCrawler.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.dto.ProductDTO;
import ru.egorov.StoreCrawler.models.Product;

@Component
public class ProductConvertor {
    // TODO: 20.11.2022 Выбор неплохой, но рекомендовал бы mapStruct как более популярное решение
    private final ModelMapper modelMapper;

    @Autowired
    public ProductConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductDTO convertToProductDTO (Class<? extends ProductDTO> clazz, Product product) {
        return modelMapper.map(product, clazz);
    }
}
