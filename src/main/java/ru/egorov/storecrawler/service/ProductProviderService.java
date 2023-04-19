package ru.egorov.storecrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.egorov.storecrawler.dto.product.ProductDto;
import ru.egorov.storecrawler.mapper.ProductMapper;
import ru.egorov.storecrawler.model.Product;
import ru.egorov.storecrawler.model.StoreType;
import ru.egorov.storecrawler.service.product.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductProviderService {
    private final DispatcherService dispatcherService;

    public Page<ProductDto> gain(StoreType storeType, Pageable pageable) {
        ProductService productService = dispatcherService.getProductsService(storeType);

        var page = productService.findAll(pageable);

        log.info("{} {} products provided", page.getTotalElements(), storeType);

        return convertToDto(storeType, page);
    }

    private Page<ProductDto> convertToDto(StoreType storeType, Page<? extends Product> page) {
        ProductMapper mapper = dispatcherService.getMapper(storeType);

        return page.map(mapper :: toDto);
    }
}
