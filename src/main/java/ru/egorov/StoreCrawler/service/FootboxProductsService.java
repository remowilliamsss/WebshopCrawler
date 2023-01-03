package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.model.FootboxProduct;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.repository.FootboxProductsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FootboxProductsService extends ProductsService {
    private final FootboxProductsRepository footboxProductsRepository;

    @Override
    // TODO: 13.12.2022 советую прикрутить пагинацию. Посмотри на Pageable, Page и @PageableDefault
    public List<FootboxProduct> findAll() {
        return footboxProductsRepository.findAll();
    }

    @Override
    public Page<FootboxProduct> findAll(Pageable pageable) {
        return footboxProductsRepository.findAll(pageable);
    }

    @Override
    // TODO: 13.12.2022 тут рановато, но для гибких фильтров познакомься с Criteria API
    public List<FootboxProduct> findAllByName(String name) {
        return footboxProductsRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    // TODO: 13.12.2022 updateMany()? Немного странно, когда в названии и метода, и сервиса
    //  дублируется имя сущности
    public List<FootboxProduct> updateProducts(List<Product> products) {
        footboxProductsRepository.deleteAll();

        footboxProductsRepository.flush();

        return footboxProductsRepository.saveAll(products
                .stream()
                .map(product -> (FootboxProduct) product)
                .collect(Collectors.toList()));
    }

    @Override
    // TODO: 13.12.2022 Optional из crud-сервиса - допустимое, но неоднозначное решение
    public Optional<FootboxProduct> findBySku(String sku) {
        return footboxProductsRepository.findBySku(sku);
    }

    @Override
    // TODO: 13.12.2022
    public Store getStore() {
        return Store.FOOTBOX;
    }
}
