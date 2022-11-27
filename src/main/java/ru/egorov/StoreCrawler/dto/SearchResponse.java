package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class SearchResponse implements Comparable<SearchResponse> {
    private String sku;
    private String itemName;
    private Map<String, Double> priceList;

    @Override
    public int compareTo(SearchResponse o) {
        return getItemName().compareTo(o.getItemName());
    }
}
