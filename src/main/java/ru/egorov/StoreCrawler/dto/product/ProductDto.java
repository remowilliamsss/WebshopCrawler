package ru.egorov.StoreCrawler.dto.product;

import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.model.StoreType;

@Getter
@Setter
public abstract class ProductDto {

    private String name;

    private String sku;

    private String category;

    private String brand;

    private String image;

    private String color;

    private Double price;

    private String priceCurrency;

    private String country;

    private String sizes;

    private String gender;

    private String url;

    private StoreType storeType;
}
