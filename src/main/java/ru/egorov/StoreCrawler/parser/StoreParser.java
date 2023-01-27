package ru.egorov.StoreCrawler.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

@Slf4j
public abstract class StoreParser {

    public Set<String> parsePages() {
        Set<String> urls = new HashSet<>();

        addCategories(urls);

        addItemPages(urls);

        return urls;
    }

    protected abstract void addCategories(Set<String> urls);

    protected abstract void addItemPages(Set<String> urls);

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
