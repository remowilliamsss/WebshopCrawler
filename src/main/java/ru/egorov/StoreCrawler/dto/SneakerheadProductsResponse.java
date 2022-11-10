package ru.egorov.StoreCrawler.dto;

import java.util.List;

public class SneakerheadProductsResponse {
    private List<SneakerheadProductDTO> products;

    public SneakerheadProductsResponse(List<SneakerheadProductDTO> products) {
        this.products = products;
    }

    public List<SneakerheadProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<SneakerheadProductDTO> products) {
        this.products = products;
    }
}
