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
// TODO: 20.11.2022 Выглядит как сервисный класс. Непонятно, почему он не в сервисе 
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

        // TODO: 20.11.2022 с типами коллекций что-то намудрил. ЗАчем лишние преобразования? 
        List<Product> products = footboxProductParser.parseProducts(new ArrayList<>(urls), getStopped());

        // TODO: 20.11.2022 То, что левому сервису нужна инфа о состоянии другого сервиса -
        //  признак ошибки в проектировании сервисов.
        //  Два метода подряд для обновления и удаления лишних тоже выглядит не самой удачной идеей.
        //  Проще уж всю таблицу очистить и заново значения загрузить. Быстрее будет
        footboxProductsService.updateProducts(products, getStopped());
        footboxProductsService.deleteOther(products, getStopped());

        System.out.println("Footbox crawler finished scanning at " + new Date());
    }

    @Override
    public String getStoreName() {
        return "Footbox";
    }
}
