package ru.egorov.StoreCrawler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.mapper.ProductMapper;
import ru.egorov.StoreCrawler.service.ProductsService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Dispatcher {
    private final List<ProductsService> productsServices;
    private final List<ProductMapper> productMappers;

    public ProductsService getService(String storeName) {
        return getService(Store.valueOf(storeName.toUpperCase()));
    }

    public ProductsService getService(Store store) {
        return productsServices.stream()
                .filter(service -> service.getStore() == store)
                .findFirst()
                .get();
    }

    public ProductMapper getMapper(String storeName) {
        return productMappers.stream()
                .filter(mapper -> mapper.getStore() == Store.valueOf(storeName.toUpperCase()))
                .findFirst()
                .get();
    }
}
