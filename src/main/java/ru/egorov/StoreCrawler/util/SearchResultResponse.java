package ru.egorov.StoreCrawler.util;

import java.util.List;

public class SearchResultResponse {
    private List<SearchResult> resultList;

    public List<SearchResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<SearchResult> resultList) {
        this.resultList = resultList;
    }
}
