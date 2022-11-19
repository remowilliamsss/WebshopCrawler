package ru.egorov.StoreCrawler.crawlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.parsers.FootboxProductParser;
import ru.egorov.StoreCrawler.services.FootboxProductsService;
import ru.egorov.StoreCrawler.util.HtmlParser;

import java.io.IOException;
import java.util.*;

@Component
public class FootboxCrawler extends Crawler {
    private final HtmlParser htmlParser;
    private final FootboxProductParser footboxProductParser;
    private final FootboxProductsService footboxProductsService;

    @Autowired
    public FootboxCrawler(HtmlParser htmlParser, FootboxProductParser footboxProductParser,
                          FootboxProductsService footboxProductsService) {
        this.htmlParser = htmlParser;
        this.footboxProductParser = footboxProductParser;
        this.footboxProductsService = footboxProductsService;
    }

    @Override
    public void scan() throws IOException {
        System.out.println("Footbox crawler started scanning at " + new Date());

        setStopped(false);

        Set<String> urls = new HashSet<>();

        urls.add("https://www.footboxshop.ru/catalog/obuv/");
        urls.add("https://www.footboxshop.ru/catalog/odezhda/");
        urls.add("https://www.footboxshop.ru/catalog/aksessuary/");

        htmlParser.addLinks(urls, "pagination__item", getStopped());
        htmlParser.addLinks(urls, "pagination__item", "PAGEN", getStopped(), 3);
        htmlParser.addLinks(urls, "catalog-card__model", getStopped());

        List<Product> products = footboxProductParser.parseProducts(new ArrayList<>(urls), getStopped());

        footboxProductsService.updateProducts(products, getStopped());
        footboxProductsService.deleteOther(products, getStopped());

        System.out.println("Footbox crawler finished scanning at " + new Date());
    }

    @Override
    public String getStoreName() {
        return "Footbox";
    }
}
