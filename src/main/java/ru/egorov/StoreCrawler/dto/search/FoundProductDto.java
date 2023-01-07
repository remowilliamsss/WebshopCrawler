package ru.egorov.StoreCrawler.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoundProductDto {
    private String name;
    private String sku;
    private String image;
    private String category;
    private String brand;
    private String color;
    private String country;
    private String gender;
    private List<FoundProductDifference> difference;

    {
        difference = Collections.emptyList();
    }
}
