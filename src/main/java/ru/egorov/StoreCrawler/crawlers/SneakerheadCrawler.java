package ru.egorov.StoreCrawler.crawlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.services.SneakerheadProductsService;
import ru.egorov.StoreCrawler.util.HtmlParser;
import ru.egorov.StoreCrawler.parsers.SneakerheadProductParser;

import java.io.IOException;
import java.util.*;

@Component
public class SneakerheadCrawler extends Crawler {
    private final HtmlParser htmlParser;
    private final SneakerheadProductParser sneakerheadProductParser;
    private final SneakerheadProductsService sneakerheadProductsService;

    @Autowired
    public SneakerheadCrawler(HtmlParser htmlParser, SneakerheadProductParser sneakerheadProductParser,
                              SneakerheadProductsService sneakerheadProductsService) {
        this.htmlParser = htmlParser;
        this.sneakerheadProductParser = sneakerheadProductParser;
        this.sneakerheadProductsService = sneakerheadProductsService;
    }

    @Override
    public void scan() throws IOException {
        System.out.println("Sneakerhead crawler started scanning at " + new Date());

        setStopped(false);

        Set<String> urls = new HashSet<>();

        urls.add("https://sneakerhead.ru/shoes/");
        urls.add("https://sneakerhead.ru/clothes/");
        urls.add("https://sneakerhead.ru/stuff/");
        urls.add("https://sneakerhead.ru/kiosk/");

        htmlParser.addLinks(urls, "pagination", getStopped());
        htmlParser.addLinks(urls, "pagination", "PAGEN", getStopped());
        htmlParser.addLinks(urls, "product-cards__item", getStopped());

        List<Product> products = sneakerheadProductParser.parseProducts(new ArrayList<>(urls), getStopped());

        sneakerheadProductsService.updateProducts(products, getStopped());
        sneakerheadProductsService.deleteOther(products, getStopped());

        System.out.println("Sneakerhead crawler finished scanning at " + new Date());
    }
}
