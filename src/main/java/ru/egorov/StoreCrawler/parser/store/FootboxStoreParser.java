package ru.egorov.StoreCrawler.parser.store;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FootboxStoreParser extends StoreParser {
    public static final String ITEM = "catalog-card__model";
    public static final String PAGINATION = "pagination__item";
    public static final String SHOES = "https://www.footboxshop.ru/catalog/obuv/";
    public static final String CLOTHES = "https://www.footboxshop.ru/catalog/odezhda/";
    public static final String STUFF = "https://www.footboxshop.ru/catalog/aksessuary/";

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add(SHOES);
        urls.add(CLOTHES);
        urls.add(STUFF);
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, PAGINATION);
        addLinks(urls, PAGINATION, PAGEN, 3);
        addLinks(urls, ITEM);
    }
}
