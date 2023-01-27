package ru.egorov.StoreCrawler.parser;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SneakerheadStoreParser extends StoreParser {

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add("https://sneakerhead.ru/shoes/");
        urls.add("https://sneakerhead.ru/clothes/");
        urls.add("https://sneakerhead.ru/stuff/");
        urls.add("https://sneakerhead.ru/kiosk/");
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, "pagination");
        addLinks(urls, "pagination", "PAGEN");
        addLinks(urls, "product-cards__item");
    }
}
