package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.StoreCrawler.crawlers.SneakerheadCrawler;
import ru.egorov.StoreCrawler.dto.SneakerheadProductDTO;
import ru.egorov.StoreCrawler.dto.SneakerheadProductsResponse;
import ru.egorov.StoreCrawler.services.SneakerheadProductsService;
import ru.egorov.StoreCrawler.util.ProductConvertor;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("crawlers")
public class CrawlersController {

    private final SneakerheadCrawler sneakerheadCrawler;
    private final SneakerheadProductsService sneakerheadProductsService;
    private final ProductConvertor productConvertor;

    @Autowired
    public CrawlersController(SneakerheadCrawler sneakerheadCrawler, SneakerheadProductsService sneakerheadProductsService, ProductConvertor productConvertor) {
        this.sneakerheadCrawler = sneakerheadCrawler;
        this.sneakerheadProductsService = sneakerheadProductsService;
        this.productConvertor = productConvertor;
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

    @GetMapping("sneakerhead/products")
    public SneakerheadProductsResponse getSneakerheadProducts() {
        return new SneakerheadProductsResponse(sneakerheadProductsService.findAll().stream()
                .map(product -> productConvertor.convertToProductDTO(SneakerheadProductDTO.class, product))
                .map(productDTO -> (SneakerheadProductDTO) productDTO).collect(Collectors.toList()));
    }
}
