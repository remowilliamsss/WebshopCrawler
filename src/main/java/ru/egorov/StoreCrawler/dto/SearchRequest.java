package ru.egorov.StoreCrawler.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchRequest {
    @NotNull(message = "Query is null")
    private String query;
}
