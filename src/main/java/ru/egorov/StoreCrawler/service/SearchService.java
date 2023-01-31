package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.dto.search.FoundProductDto;
import ru.egorov.StoreCrawler.dto.product.ProductDto;
import ru.egorov.StoreCrawler.dto.search.SearchResultDto;
import ru.egorov.StoreCrawler.dto.search.FoundProductDifference;
import ru.egorov.StoreCrawler.mapper.ProductMapper;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.StoreType;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final DispatcherService dispatcherService;
    private final List<ProductService> productServices;

    public List<FoundProductDto> search(String query) {
        log.info("Search for \"{}\" starts", query);

        List<FoundProductDto> foundProductDtos = new ArrayList<>();

        productServices.stream()
                .flatMap(productService -> productService.findAllByName(query)
                        .stream())
                .collect(Collectors.toList());

        productServices.stream()
            .map(productsService -> productsService.findAllByName(query))
            .filter(products -> !products.isEmpty())
            .forEach(products -> {// TODO: 13.12.2022 избыточные {}
                // TODO: 13.12.2022 познакомься с flatMap() :)
                products.forEach(product -> {
                    // TODO: 13.12.2022 условие ифа вынеси в переменную
                    if (foundProductDtos.stream()
                            .noneMatch(foundProductDto -> foundProductDto.getSku().equals(product.getSku()))) {
                        // TODO: 13.12.2022 кажется, огород с id-else здесь лишний, можно попробовать без него
                        foundProductDtos.add(createFoundProduct(product));
                    } else {
                        foundProductDtos.stream()
                                .filter(foundProductDto -> foundProductDto.getSku().equals(product.getSku()))
                                .findFirst()
                                // TODO: 13.12.2022 вызов get()
                                .get()
                                .getDifference()
                                .add(createProductDifferences(product));
                    }
                });
            });

        log.info("Search for \"{}\" finished with {} results", query, foundProductDtos.size());

        // TODO: 13.12.2022 зачем в этой схеме SearchResponse?
        return foundProductDtos;
    }

    public List<ProductDto> findByStore(StoreType storeType, Pageable pageable) {
        log.info("Search for \"{}\" starts", storeType);

        ProductService productService = dispatcherService.getProductsService(storeType);
        var products = productService.findAll(pageable)
                .getContent();

        List<ProductDto> productDtos = convertToDto(storeType, products);

        log.info("Search for \"{}\" finished with {} results", storeType, productDtos.size());

        return productDtos;
    }

    private List<ProductDto> convertToDto(StoreType storeType, List<? extends Product> products) {
        ProductMapper mapper = dispatcherService.getMapper(storeType);

        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FoundProductDto> findBySku(String sku) {
        log.info("Search for \"{}\" starts", sku);

        List<FoundProductDto> foundProductDtos = new ArrayList<>(1);

        productServices.stream()
                //findAllBySkuIn для репы. Сейчас куча лишних запросов в базу
                .map(productsService -> productsService.findBySku(sku))
                .filter(Objects::nonNull)
                .forEach(product -> {
                    // TODO: 13.12.2022 кажется, огород с id-else здесь лишний, можно попробовать без него
                    if (foundProductDtos.isEmpty()) {
                        foundProductDtos.add(createFoundProduct(product));
                    } else {
                        foundProductDtos.get(0)
                                .getDifference()
                                .add(createProductDifferences(product));
                    }
                });

        log.info("Search for \"{}\" finished", sku);

        return foundProductDtos;
    }

    // TODO: 13.12.2022 выглядит как логика для маппера
    private FoundProductDto createFoundProduct(Product product) {
        List<FoundProductDifference> differences = new ArrayList<>();

        differences.add(createProductDifferences(product));

        // TODO: 13.12.2022 @Builder в Lombok
        return new FoundProductDto(
                product.getName(), product.getSku(),
                product.getImage(), product.getCategory(),
                product.getBrand(), product.getColor(),
                product.getCountry(), product.getGender(),
                differences);
    }

    private FoundProductDifference createProductDifferences(Product product) {
        return new FoundProductDifference(
                product.getStoreType(),
                product.getPrice(), product.getPriceCurrency(),
                product.getSize(), product.getUrl());
    }
}
