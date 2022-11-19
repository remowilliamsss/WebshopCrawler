package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.FootboxProductDTO;
import ru.egorov.StoreCrawler.dto.ProductsResponse;
import ru.egorov.StoreCrawler.exceptions.*;
import ru.egorov.StoreCrawler.http.NothingFoundResponse;
import ru.egorov.StoreCrawler.http.ErrorResponse;
import ru.egorov.StoreCrawler.http.SearchRequest;
import ru.egorov.StoreCrawler.http.SearchResultResponse;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.services.ProductsService;
import ru.egorov.StoreCrawler.util.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
public class ProductsController {
    private final List<ProductsService> productsServices;
    private final ProductConvertor productConvertor;

    @Autowired
    public ProductsController(ProductConvertor productConvertor, ProductsService... productsServices) {
        this.productConvertor = productConvertor;
        this.productsServices = new ArrayList<>(List.of(productsServices));
    }

    @PostMapping("/search")
    public SearchResultResponse search(@RequestBody SearchRequest request) {
        String query = request.getQuery();

        if (query == null)
            throw new NullQueryException();

        SearchResultResponse response = new SearchResultResponse();
        List<SearchResult> resultList = new ArrayList<>();

        for (ProductsService productsService : productsServices) {
            List<Product> foundProducts = productsService.findAllByName(query);

            if (!foundProducts.isEmpty()) {
                String name = productsService.getStoreName();

                for (Product product : foundProducts) {
                    if (resultList.stream().noneMatch(searchResult -> searchResult.getSku().equals(product.getSku()))) {
                        SearchResult searchResult = new SearchResult();
                        searchResult.setSku(product.getSku());
                        searchResult.setItemName(product.getName());

                        Map<String, Double> priceList = new HashMap<>();
                        priceList.put(name, product.getPrice());
                        searchResult.setPriceList(priceList);

                        resultList.add(searchResult);
                    } else {
                        SearchResult searchResult = resultList.stream().filter(
                                sr -> sr.getSku().equals(product.getSku())).findFirst().get();

                        searchResult.getPriceList().put(name, product.getPrice());
                    }
                }
            }
        }
        if (resultList.isEmpty())
            throw new NothingFoundException();

        Collections.sort(resultList);

        response.setResultList(resultList);

        return response;
    }

    @GetMapping("/{storeName}")
    public ProductsResponse getProducts(@PathVariable("storeName") String storeName) {
        Optional<ProductsService> productsService = productsServices.stream().filter(service -> service.getStoreName()
                        .equalsIgnoreCase(storeName)).findFirst();

        if (productsService.isPresent())
            return new ProductsResponse(productsService.get().findAll().stream()
                    .map(product -> productConvertor.convertToProductDTO(FootboxProductDTO.class, product))
                    .collect(Collectors.toList()));

        throw new StoreNotFoundException();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NullQueryException e) {
        ErrorResponse response = new ErrorResponse("Query is null", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<NothingFoundResponse> handleException(NothingFoundException e) {
        NothingFoundResponse response = new NothingFoundResponse();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(StoreNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "The store with this name is not supported", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
