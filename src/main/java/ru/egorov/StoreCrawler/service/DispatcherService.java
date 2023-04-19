package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.mapper.ProductMapper;
import ru.egorov.StoreCrawler.service.product.ProductService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DispatcherService {
    private final Map<String, ProductService> productsServices;
    private final Map<String, ProductMapper> productMappers;

    public ProductService getProductsService(StoreType storeType) {
        return switch (storeType) {
            case sneakerhead -> productsServices.get("sneakerheadProductService");
            case footbox -> productsServices.get("footboxProductService");
        };
    }

    public ProductMapper getMapper(StoreType storeType) {
        return switch (storeType) {
            case footbox -> productMappers.get("footboxProductMapperImpl");
            default -> productMappers.get("productMapperImpl");
        };
    }
}
