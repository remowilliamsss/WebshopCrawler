package ru.egorov.StoreCrawler.dto.search;

import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.model.StoreType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FoundProductDto {

    private String name;

    private String sku;

    private String image;

    private String category;

    private String brand;

    private String color;

    private String country;

    private String gender;

    private List<Difference> differences;

    {
        differences = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Difference {

        private StoreType storeType;

        private Double price;

        private String priceCurrency;

        private String size;

        private String url;
    }
}
