package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.mapper.ProductMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DispatcherService {
    private final Map<String, ProductsService> productsServices;
    private final Map<String, ProductMapper> productMappers;

    public ProductsService getProductsService(StoreType storeType) {
        return switch (storeType) {
            case sneakerhead -> productsServices.get("sneakerheadProductsService");
            case footbox -> productsServices.get("footboxProductsService");
        };
    }

    public ProductMapper getMapper(StoreType storeType) {
        return switch (storeType) {
            case sneakerhead -> productMappers.get("sneakerheadProductMapperImpl");
            case footbox -> productMappers.get("footboxProductMapperImpl");
        };
    }
}
