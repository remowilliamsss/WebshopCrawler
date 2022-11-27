package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.Dispatcher;
import ru.egorov.StoreCrawler.parser.StoreParser;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Crawler {
    private static final Logger log = LoggerFactory.getLogger(Crawler.class);
    private final List<StoreParser> parsers;
    private final Dispatcher dispatcher;

    @Scheduled(cron = "@daily")
    public void crawl() {
        log.info("Crawling starts...");

        parsers.forEach(parser ->
            new Thread(() -> {
                dispatcher.getService(parser.getStore())
                    .updateProducts(parser
                        .parseProducts());
            }).start());
    }
}
