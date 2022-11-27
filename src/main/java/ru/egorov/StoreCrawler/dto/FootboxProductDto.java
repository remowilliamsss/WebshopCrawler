package ru.egorov.StoreCrawler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FootboxProductDto extends ProductDto {
    private String composition;
    private String coloring;
}
