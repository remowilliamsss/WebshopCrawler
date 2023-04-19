package ru.egorov.storecrawler.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.storecrawler.model.FootboxProduct;
import ru.egorov.storecrawler.model.Product;
import ru.egorov.storecrawler.repository.FootboxProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FootboxProductService implements ProductService {
    private final FootboxProductRepository footboxProductRepository;

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
