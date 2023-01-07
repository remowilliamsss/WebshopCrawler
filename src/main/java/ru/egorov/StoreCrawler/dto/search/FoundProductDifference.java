package ru.egorov.StoreCrawler.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoundProductDifference {
    private String storeName;
    private Double price;
    private String priceCurrency;
    private String sizes;
    private String url;
}
