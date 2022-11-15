package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.StoreCrawler.crawlers.FootboxCrawler;
import ru.egorov.StoreCrawler.dto.FootboxProductDTO;
import ru.egorov.StoreCrawler.dto.ProductsResponse;
import ru.egorov.StoreCrawler.services.FootboxProductsService;
import ru.egorov.StoreCrawler.util.ProductConvertor;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("crawlers/footbox")
public class FootboxCrawlerController {
    private final FootboxCrawler footboxCrawler;
    private final FootboxProductsService footboxProductsService;
    private final ProductConvertor productConvertor;

    @Autowired
    public FootboxCrawlerController(FootboxCrawler footboxCrawler, FootboxProductsService footboxProductsService, ProductConvertor productConvertor) {
        this.footboxCrawler = footboxCrawler;
        this.footboxProductsService = footboxProductsService;
        this.productConvertor = productConvertor;
    }


    @GetMapping("/scan")
    public String scan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    footboxCrawler.scan();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        return "Footbox scan started";
    }

    @GetMapping("/stop_scan")
    public String stopScan() {
        footboxCrawler.stopScan();

        return "Footbox scan stopped";
    }

    @GetMapping("/start")
    public String start() {
        footboxCrawler.start();

        return "Footbox crawler started";
    }

    @GetMapping("/stop")
    public String stop() {
        footboxCrawler.stop();

        return "Footbox crawler stopped";
    }

    @GetMapping("/products")
    public ProductsResponse getSneakerheadProducts() {
        return new ProductsResponse(footboxProductsService.findAll().stream()
                .map(product -> productConvertor.convertToProductDTO(FootboxProductDTO.class, product))
                .collect(Collectors.toList()));
    }
}
