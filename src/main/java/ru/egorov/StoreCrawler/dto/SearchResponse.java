package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// TODO: 13.12.2022 советую располагать аннотации по длине от коротких к длинным
@AllArgsConstructor
@Getter
@Setter
public class SearchResponse {
    // TODO: 13.12.2022 всегда делай дефолтную инициализацию коллекций.
    //  Иначе рано или поздно поймаешь NullPointerException
    private List<FoundProduct> foundProductList;
}
