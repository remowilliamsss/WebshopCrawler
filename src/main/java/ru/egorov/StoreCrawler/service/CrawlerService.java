package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.parser.product.ProductParser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final List<ProductParser> parsers;
    private final DispatcherService dispatcherService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Scheduled(cron = "@daily")
    public void crawl() {
        log.info("Crawling starts...");

        parsers.forEach(parser ->
                executorService.execute(() ->
                        dispatcherService.getProductsService(parser.getStore())
                                .updateProducts(parser.parseProducts())));
    }
}
