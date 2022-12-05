package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public List<FootboxProduct> findAll() {
        return footboxProductsRepository.findAll();
    }

    @Override
    public List<FootboxProduct> findAll(Integer page, Integer productPerPage) {
        return footboxProductsRepository.findAll(PageRequest
                .of(page, productPerPage)).getContent();
    }

    @Override
    public List<FootboxProduct> findAllByName(String name) {
        return footboxProductsRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public List<FootboxProduct> updateProducts(List<Product> products) {
        footboxProductsRepository.deleteAll();

        footboxProductsRepository.flush();

        return footboxProductsRepository.saveAll(products
                .stream()
                .map(product -> (FootboxProduct) product)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<FootboxProduct> findBySku(String sku) {
        return footboxProductsRepository.findBySku(sku);
    }

    @Override
    public Store getStore() {
        return Store.FOOTBOX;
    }
}
