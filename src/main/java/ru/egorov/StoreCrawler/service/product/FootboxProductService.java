package ru.egorov.StoreCrawler.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.model.product.FootboxProduct;
import ru.egorov.StoreCrawler.model.product.Product;
import ru.egorov.StoreCrawler.repository.product.FootboxProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FootboxProductService implements ProductService {
    private final FootboxProductRepository footboxProductRepository;

    @Override
    public List<FootboxProduct> findAll() {
        return footboxProductRepository.findAll();
    }

    @Override
    public Page<FootboxProduct> findAll(Pageable pageable) {
        return footboxProductRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public List<FootboxProduct> updateAll(List<Product> products) {
        footboxProductRepository.deleteAll();

        footboxProductRepository.flush();

        return footboxProductRepository.saveAll(products
                .stream()
                .map(product -> (FootboxProduct) product)
                .collect(Collectors.toList()));
    }
}
