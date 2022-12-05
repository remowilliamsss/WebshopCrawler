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
public class FoundProduct implements Comparable<FoundProduct> {
    private String name;
    private String sku;
    private String image;
    private String category;
    private String brand;
    private String color;
    private String country;
    private String gender;
    private List<ProductDifferences> differences;

    @Override
    public int compareTo(FoundProduct o) {
        return getDifferences().get(0)
                .compareTo(o.getDifferences().get(0));
    }
}
