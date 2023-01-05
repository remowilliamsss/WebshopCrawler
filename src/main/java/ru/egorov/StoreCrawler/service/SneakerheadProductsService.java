package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.SneakerheadProduct;
import ru.egorov.StoreCrawler.repository.SneakerheadProductsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// TODO: 13.12.2022 не стал ревьюить, не думаю, что есть большие отличия от Footbox
public class SneakerheadProductsService extends ProductsService {
    private final SneakerheadProductsRepository sneakerheadProductsRepository;

    @Override
    public Page<SneakerheadProduct> findAll(Pageable pageable) {
        return sneakerheadProductsRepository.findAll(pageable);
    }

    @Override
    public List<SneakerheadProduct> findAllByName(String name) {
        return sneakerheadProductsRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public List<SneakerheadProduct> updateProducts(List<Product> products) {
        sneakerheadProductsRepository.deleteAll();

        sneakerheadProductsRepository.flush();

        return sneakerheadProductsRepository.saveAll(products
                .stream()
                .map(product -> (SneakerheadProduct) product)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<SneakerheadProduct> findBySku(String sku) {
        return sneakerheadProductsRepository.findBySku(sku);
    }

    @Override
    public Store getStore() {
        return Store.SNEAKERHEAD;
    }
}
