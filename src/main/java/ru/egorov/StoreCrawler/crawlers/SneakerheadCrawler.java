package ru.egorov.StoreCrawler.crawlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;
import ru.egorov.StoreCrawler.services.SneakerheadProductsService;
import ru.egorov.StoreCrawler.util.HtmlParser;
import ru.egorov.StoreCrawler.util.SneakerheadProductParser;

import java.io.IOException;
import java.util.*;

@Component
public class SneakerheadCrawler {

    private final HtmlParser htmlParser;
    private final SneakerheadProductParser sneakerheadProductParser;
    private final SneakerheadProductsService sneakerheadProductsService;
    private Boolean isStopped;
    private Timer timer;

    @Autowired
    public SneakerheadCrawler(HtmlParser htmlParser, SneakerheadProductParser sneakerheadProductParser,
                              SneakerheadProductsService sneakerheadProductsService) {
        this.htmlParser = htmlParser;
        this.sneakerheadProductParser = sneakerheadProductParser;
        this.sneakerheadProductsService = sneakerheadProductsService;
    }

    /*    Метод добавляет в базу данных все товары с сайта https://sneakerhead.ru/, которых еще нет в бд,
    обновляет информацию у существующих, удаляет товары, которых нет на сайте.*/
    public void scan() throws IOException {
        System.out.println("Sneakerhead crawler started scanning at " + new Date());

        isStopped = false;

        Set<String> urls = new HashSet<>();

        htmlParser.addLinks("https://sneakerhead.ru/", urls, "new-header__navbar", isStopped);
        htmlParser.addLinks(urls, "links", isStopped);
        htmlParser.addLinks(urls, "links", "PAGEN", isStopped);
        htmlParser.addLinks(urls, "product-cards__item", isStopped);

        List<SneakerheadProduct> products = sneakerheadProductParser.parseProducts(new ArrayList<>(urls), isStopped);

        sneakerheadProductsService.updateProducts(products, isStopped);
        sneakerheadProductsService.deleteOther(products, isStopped);

        System.out.println("Sneakerhead crawler finished scanning at " + new Date());
    }

    /*    Останавливает текущее сканирование.*/
    public void stopScan() {
        isStopped = true;
    }

    /*  Запускает автоматическое сканирование сайта с интервалом один раз в сутки.*/
    public void start() {
        stop();

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    scan();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 24*60*60*1000);
    }

    /*  Останавливает автоматическое сканирование сайта.*/
    public void stop() {
        if (timer != null)
            timer.cancel();

        stopScan();
    }
}
