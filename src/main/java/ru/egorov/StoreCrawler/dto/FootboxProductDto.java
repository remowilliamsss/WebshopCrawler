package ru.egorov.StoreCrawler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO: 13.12.2022 сеттер лишний для запросов
public class FootboxProductDto extends ProductDto {
    private String composition;
    private String coloring;
}
