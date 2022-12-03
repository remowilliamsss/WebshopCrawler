package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductDifferences implements Comparable<ProductDifferences> {
    private String storeName;
    private Double price;
    private String priceCurrency;
    private String sizes;
    private String url;

    @Override
    public int compareTo(ProductDifferences o) {
        return getPrice().compareTo(o.getPrice());
    }
}
