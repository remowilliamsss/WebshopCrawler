package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.dto.product.ProductResponse;
import ru.egorov.StoreCrawler.dto.search.FoundProductDto;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.mapper.FoundProductMapper;
import ru.egorov.StoreCrawler.mapper.product.ProductMapper;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.service.product.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final DispatcherService dispatcherService;
    private final List<ProductService> productServices;
    private final FoundProductMapper foundProductMapper;

    public static final String SEARCH_START = "Search for \"{}\" is starting.";
    public static final String SEARCH_FINISH = "Search for \"{}\" finished with {} results.";

    public List<FoundProductDto> search(String query) {
        log.info(SEARCH_START, query);

        List<FoundProductDto> foundProductDtos = new ArrayList<>();

        productServices.stream()
                .flatMap(productService -> productService.findAllByName(query)
                        .stream())
                .forEach(product -> addFoundProduct(foundProductDtos, product));

        log.info(SEARCH_FINISH, query, foundProductDtos.size());

        return foundProductDtos;
    }

    public List<FoundProductDto> findBySku(String sku) {
        log.info(SEARCH_START, sku);

        List<FoundProductDto> foundProductDtos = new ArrayList<>(1);

        productServices.stream()
                .map(productsService -> productsService.findBySku(sku))
                .filter(Objects::nonNull)
                .forEach(product -> addFoundProduct(foundProductDtos, product));

        log.info("Search for \"{}\" finished.", sku);

        return foundProductDtos;
    }

    private void addFoundProduct(List<FoundProductDto> foundProductDtos, Product product) {
        FoundProductDto foundProductDto = prepareFoundProduct(foundProductDtos, product);

        foundProductDto.getDifferences()
                .add(foundProductMapper.extractDifference(product));

        foundProductDtos.add(foundProductDto);
    }

    private FoundProductDto prepareFoundProduct(List<FoundProductDto> foundProductDtos, Product product) {
        return foundProductDtos.stream()
                .filter(foundProductDto -> foundProductDto.getSku()
                        .equals(product.getSku()))
                .findFirst()
                .orElse(foundProductMapper.toDto(product));
    }

    public ProductResponse findByStore(StoreType storeType, Pageable pageable) {
        log.info(SEARCH_START, storeType);

        ProductService productService = dispatcherService.getProductsService(storeType);

        var page = productService.findAll(pageable);

        var products = page.getContent();
        List<ProductDto> productDtos = convertToDto(storeType, products);

        ProductResponse response = new ProductResponse(productDtos, page.getTotalPages());

        log.info(SEARCH_FINISH, storeType, page.getTotalElements());

        return response;
    }

    private List<ProductDto> convertToDto(StoreType storeType, List<? extends Product> products) {
        ProductMapper mapper = dispatcherService.getMapper(storeType);

        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
