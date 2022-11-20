package ru.egorov.StoreCrawler.util;

import java.util.Map;

// TODO: 20.11.2022 не очень понимаю назначение этого класса
public class SearchResult implements Comparable<SearchResult> {
    private String sku;
    private String itemName;
    private Map<String, Double> priceList;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    public void setPriceList(Map<String, Double> priceList) {
        this.priceList = priceList;
    }

    @Override
    public int compareTo(SearchResult o) {
        return getItemName().compareTo(o.getItemName());
    }
}
