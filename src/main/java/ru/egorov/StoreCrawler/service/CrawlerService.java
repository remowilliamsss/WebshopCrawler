package ru.egorov.StoreCrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.service.parser.product.ProductParserService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CrawlerService {
    private final List<ProductParserService> parsers;
    private final ExecutorService executorService;

    @Autowired
    public CrawlerService(List<ProductParserService> parsers) {
        this(parsers, 8);
    }

    public CrawlerService(List<ProductParserService> parsers, int maxThreadCount) {
        this.parsers = parsers;
        int threadCount = parsers.size();

        if (maxThreadCount < threadCount) {
            threadCount = maxThreadCount;
        }

        executorService = Executors.newFixedThreadPool(threadCount);
    }

    @Scheduled(cron = "@daily")
    public void crawl() {
        parsers.forEach(parser ->
                executorService.execute(parser::parseAll));
    }
}
