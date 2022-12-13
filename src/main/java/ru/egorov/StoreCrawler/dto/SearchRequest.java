package ru.egorov.StoreCrawler.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchRequest {
    // TODO: 13.12.2022 месседж избыточен. как и сеттер
    // TODO: 13.12.2022 кажется, тебе нужен @NotEmpty
    @NotNull(message = "Query is null")
    private String query;
}
