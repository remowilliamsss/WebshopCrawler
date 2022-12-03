package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchResponse {
    private List<FoundProduct> foundProductList;
}
