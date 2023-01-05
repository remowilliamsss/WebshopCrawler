package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoundProduct {
    private String name;
    private String sku;
    private String image;
    private String category;
    private String brand;
    private String color;
    private String country;
    private String gender;
    private List<ProductDifferences> differences;
}
