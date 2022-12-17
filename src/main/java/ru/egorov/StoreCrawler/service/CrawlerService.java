package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.parser.StoreParser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);
    private final List<StoreParser> parsers;
    private final DispatcherService dispatcherService;

    @Scheduled(cron = "@daily")
    public void crawl() {
        log.info("Crawling starts...");

        parsers.forEach(parser ->
                // TODO: 13.12.2022 executorService. Никогда не работай с голыми тредами в spring-приложении
            new Thread(() -> {// TODO: 13.12.2022 избыточные {} в однострочной лямбде
                dispatcherService.getProductsService(parser.getStore())
                    .updateProducts(parser
                        .parseProducts());
            }).start());
    }
}
