package ru.egorov.StoreCrawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.egorov.StoreCrawler.parser.product.ProductParser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final List<ProductParser> parsers;

    @Scheduled(cron = "@daily")
    public void crawl() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        parsers.forEach(parser ->
                executorService.execute(parser::parseProducts));
    }
}
