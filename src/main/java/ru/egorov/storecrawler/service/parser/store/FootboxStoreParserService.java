package ru.egorov.storecrawler.service.parser.store;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FootboxStoreParserService extends StoreParserService {
    public static final String SHOES = "https://www.footboxshop.ru/catalog/obuv/";
    public static final String CLOTHES = "https://www.footboxshop.ru/catalog/odezhda/";
    public static final String STUFF = "https://www.footboxshop.ru/catalog/aksessuary/";
    public static final String ITEM = "catalog-card__model";
    public static final String PAGINATION = "pagination__item";

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
