package ru.egorov.StoreCrawler.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.model.StoreType;

@Getter
@Setter
@AllArgsConstructor
public class FoundProductDifference {

    private StoreType storeType;

    private Double price;

    private String priceCurrency;

    private String sizes;

    private String url;
}
