package ru.egorov.StoreCrawler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping("crawlers")
public class CrawlersController {

    @GetMapping("/test")
    public String test() throws IOException {
        return "test";
    }
}
