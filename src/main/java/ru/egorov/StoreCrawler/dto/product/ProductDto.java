package ru.egorov.StoreCrawler.dto.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.model.StoreType;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "product")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FootboxProductDto.class, name = "FootboxProductDto"),
        @JsonSubTypes.Type(value = SneakerheadProductDto.class, name = "SneakerheadProductDto")
})
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

    private String size;

    private String gender;

    private String url;

    private StoreType storeType;
}
