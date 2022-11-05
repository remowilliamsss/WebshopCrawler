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

    @GetMapping("/sneakerhead/scan")
    public String scan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sneakerheadCrawler.scan();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        return "Sneakerhead scan started";
    }

    @GetMapping("/sneakerhead/stop_scan")
    public String stopScan() {
            sneakerheadCrawler.stopScan();

            return "Sneakerhead scan stopped";
    }

    @GetMapping("sneakerhead/start")
    public String start() {
        sneakerheadCrawler.start();

        return "Sneakerhead crawler started";
    }

    @GetMapping("sneakerhead/stop")
    public String stop() {
            sneakerheadCrawler.stop();

            return "Sneakerhead crawler stopped";
    }
}
