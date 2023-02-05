package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.dto.ProductResponse;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.mapper.ProductMapper;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.product.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductProviderService {
    private final DispatcherService dispatcherService;

    public ProductResponse gain(StoreType storeType) {
        ProductService productService = dispatcherService.getProductsService(storeType);

        var products = productService.findAll();

        List<ProductDto> productDtos = convertToDto(storeType, products);

        ProductResponse response = new ProductResponse(productDtos, 1);

        log.info("{} {} products provided", productDtos.size(), storeType);

        return response;
    }

    public ProductResponse gain(StoreType storeType, Pageable pageable) {
        ProductService productService = dispatcherService.getProductsService(storeType);

        var page = productService.findAll(pageable);

        var products = page.getContent();
        List<ProductDto> productDtos = convertToDto(storeType, products);

        ProductResponse response = new ProductResponse(productDtos, page.getTotalPages());

        log.info("{} {} products provided", page.getTotalElements(), storeType);

        return response;
    }

    private List<ProductDto> convertToDto(StoreType storeType, List<? extends Product> products) {
        ProductMapper mapper = dispatcherService.getMapper(storeType);

        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
