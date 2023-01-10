package ru.egorov.StoreCrawler.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.model.Product;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class StoreParser {

    public List<Product> parseProducts() {
        log.info("{} scanning starts", getStore());

        List<Product> products = parseProducts(parsePages());

        log.info("{} scanning finished", getStore());

        return products;
    }

    public Set<String> parsePages() {
        Set<String> urls = new HashSet<>();

        addCategories(urls);

        addItemPages(urls);

        return urls;
    }

    protected abstract void addCategories(Set<String> urls);

    protected abstract void addItemPages(Set<String> urls);

    public abstract StoreType getStore();

    // TODO: 13.12.2022 возможно, стоит разбить парсер магазина и парсер продукта
    public abstract Product parseProduct(String url);

    public List<Product> parseProducts (Collection<String> urls) {
        return urls.parallelStream()
                .map(this::parseProduct)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    protected String parseFromItemprop(Document doc, String itemprop) {
        return doc.getElementsByAttributeValue("itemprop", itemprop)
                .get(0)
                .attr("content");
    }

    protected void addLinks(String url, Set<String> set, String elementClassName) {
        try {
            Jsoup.connect(url)
                    .timeout(10000)
                    .get()
                    .getElementsByClass(elementClassName)
                    .select("a[href]")
                    .forEach(element -> {
                        String link = element.absUrl("href");
                        set.add(link);
                        log.debug("Added url: {}", link);
                    });

        } catch (IOException e) {
            log.error("Error for url {} with message: \"{}\"", url, e.getMessage());
        }
    }

    protected void addLinks(Set<String> set, String elementClassName) {
        new HashSet<>(set).forEach(url -> addLinks(url, set, elementClassName));
    }

    protected void addLinks(Set<String> set, String elementClassName, String containedString) {
        new HashSet<>(set).stream()
                .filter(url -> url.contains(containedString))
                .forEach(url -> addLinks(url, set, elementClassName));
    }

    protected void addLinks(Set<String> set, String elementClassName, String containedString, int times) {
        for (int i = 0; i < times; i++) {
            addLinks(set, elementClassName, containedString);
        }
    }
}
