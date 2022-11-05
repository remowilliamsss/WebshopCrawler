package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.StoreCrawler.crawlers.SneakerheadCrawler;

import java.io.IOException;

@RestController
@RequestMapping("crawlers")
public class CrawlersController {

    private final SneakerheadCrawler sneakerheadCrawler;

    @Autowired
    public CrawlersController(SneakerheadCrawler sneakerheadCrawler) {
        this.sneakerheadCrawler = sneakerheadCrawler;
    }

    @GetMapping("/sneakerhead/crawl")
    public String crawl() {
        try {
            sneakerheadCrawler.crawl();

            return "Sneakerhead scan completed";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @GetMapping("sneakerhead/start")
    public String start() {
        try {
            sneakerheadCrawler.start();

            return "Sneakerhead crawler started";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("sneakerhead/stop")
    public String stop() {
            sneakerheadCrawler.stop();

            return "Sneakerhead crawler stopped";
    }
}
