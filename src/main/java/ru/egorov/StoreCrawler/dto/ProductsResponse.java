package ru.egorov.StoreCrawler.dto;

import java.util.List;

// TODO: 20.11.2022 не очень понимаю необходимость написания кастомной обертки над респонсом
public class ProductsResponse {
    private List<ProductDTO> products;

    public ProductsResponse(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
