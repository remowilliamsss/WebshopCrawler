package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.mapper.ProductMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatcherService {
    // TODO: 13.12.2022 с работой диспетчера надо подумать отдельно. сейчас выглядит очень неэффективным
    private final List<ProductsService> productsServices;
    private final List<ProductMapper> productMappers;

    // TODO: 13.12.2022 передавай enum параметром и будет тебе счастье
    public ProductsService getProductsService(String storeName) {
        return getProductsService(Store.valueOf(storeName.toUpperCase()));
    }

    public ProductsService getProductsService(Store store) {
        // TODO: 13.12.2022 чем тебе switch-case не мил?
        //  храни сервисы не в листе, а в мапе и жизнь станет проще.
        //  Спринг ключом подставит имя бина.
        //  Оно обычно создается как название класса с маленькой буквы
        return productsServices.stream()
                .filter(service -> service.getStore() == store)
                .findFirst()
                .get();
    }

    public ProductMapper getMapper(String storeName) {
        return productMappers.stream()
                .filter(mapper -> mapper.getStore() == Store.valueOf(storeName.toUpperCase()))
                .findFirst()
                // TODO: 13.12.2022 честно говоря, на геты из опшнала аж триггерит:)
                .get();
    }
}
