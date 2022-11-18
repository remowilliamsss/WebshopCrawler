package ru.egorov.StoreCrawler.util;

import java.util.Map;

public class SearchResult {
    private String itemName;
    private Map<String, Double> priceList;

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
}
