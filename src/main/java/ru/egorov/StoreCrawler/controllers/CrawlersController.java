package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.crawlers.Crawler;
import ru.egorov.StoreCrawler.exceptions.StoreNotFoundException;
import ru.egorov.StoreCrawler.http.ErrorResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("crawlers")
public class CrawlersController {
    private final List<Crawler> crawlers;

    @Autowired
    public CrawlersController(Crawler... crawlers) {
        this.crawlers = new ArrayList<>(List.of(crawlers));
    }

    @GetMapping("{storeName}/scan")
    public String scan(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        crawler.scan();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            return crawler.getStoreName() + " scan started";
        }

        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/stop_scan")
    public String stopScan(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.stopScan();

            return crawler.getStoreName() + " scan stopped";
        }
        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/start")
    public String start(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.start();

            return crawler.getStoreName() + " crawler started";
        }
        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/stop")
    public String stop(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.stop();

            return crawler.getStoreName() + " crawler stopped";
        }
        throw new StoreNotFoundException();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(StoreNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "The store with this name is not supported", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}