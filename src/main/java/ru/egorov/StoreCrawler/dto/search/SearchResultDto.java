package ru.egorov.StoreCrawler.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchResultDto {

    private List<FoundProductDto> foundProductDtosList;

    {
        foundProductDtosList = Collections.emptyList();
    }
}
