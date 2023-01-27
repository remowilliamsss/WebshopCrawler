package ru.egorov.StoreCrawler.parser;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FootboxStoreParser extends StoreParser {

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add("https://www.footboxshop.ru/catalog/obuv/");
        urls.add("https://www.footboxshop.ru/catalog/odezhda/");
        urls.add("https://www.footboxshop.ru/catalog/aksessuary/");
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, "pagination__item");
        addLinks(urls, "pagination__item", "PAGEN", 3);
        addLinks(urls, "catalog-card__model");
    }
}
