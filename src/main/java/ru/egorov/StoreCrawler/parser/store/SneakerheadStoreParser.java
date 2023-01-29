package ru.egorov.StoreCrawler.parser.store;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SneakerheadStoreParser extends StoreParser {
    public static final String ITEM = "product-cards__item";
    public static final String PAGINATION = "pagination";
    public static final String SHOES = "https://sneakerhead.ru/shoes/";
    public static final String STUFF = "https://sneakerhead.ru/stuff/";
    public static final String KIOSK = "https://sneakerhead.ru/kiosk/";
    public static final String CLOTHES = "https://sneakerhead.ru/clothes/";

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add(SHOES);
        urls.add(CLOTHES);
        urls.add(STUFF);
        urls.add(KIOSK);
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, PAGINATION);
        addLinks(urls, PAGINATION, PAGEN);
        addLinks(urls, ITEM);
    }
}
