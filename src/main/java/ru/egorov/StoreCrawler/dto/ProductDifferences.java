package ru.egorov.StoreCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
// TODO: 13.12.2022 никогда не именуй сущности во множественном числе.
//  Исключение разве что классы, содержащие только константы
public class ProductDifferences {
    private String storeName;
    private Double price;
    private String priceCurrency;
    private String sizes;
    private String url;
}
