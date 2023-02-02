package ru.egorov.StoreCrawler.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {

    private List<ProductDto> products;

    private int pageCount;
}
