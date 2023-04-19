package ru.egorov.storecrawler.dto.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.storecrawler.model.StoreType;

@Getter
@Setter
@Schema(description = "Информация о товаре")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "product")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FootboxProductDto.class, name = "FootboxProductDto"),
})
public class ProductDto {

    @Schema(description = "наименование")
    private String name;

    @Schema(description = "артикул")
    private String sku;

    @Schema(description = "категория")
    private String category;

    @Schema(description = "бренд")
    private String brand;

    @Schema(description = "url изображения")
    private String image;

    @Schema(description = "цвет")
    private String color;

    @Schema(description = "цена")
    private Double price;

    @Schema(description = "валюта")
    private String priceCurrency;

    @Schema(description = "страна")
    private String country;

    @Schema(description = "размеры")
    private String size;

    @Schema(description = "пол")
    private String gender;

    @Schema(description = "url товара на сайте магазина")
    private String url;

    @Schema(description = "название магазина")
    private StoreType storeType;
}
