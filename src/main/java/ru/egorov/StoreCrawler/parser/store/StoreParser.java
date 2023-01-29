package ru.egorov.StoreCrawler.parser.store;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import ru.egorov.StoreCrawler.exception.FailedConnectionException;

import java.io.IOException;
import java.util.*;

@Slf4j
public abstract class StoreParser {
    public static final String HREF = "href";
    public static final String PAGEN = "PAGEN";
    public static final String A_HREF = "a[href]";
    public static final String ADDED_URL = "Added url: {}";
    public static final String FAILED_CONNECTION = "Failed connection to {}:";

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
                    .select(A_HREF)
                    .forEach(element -> {
                        String link = element.absUrl(HREF);
                        set.add(link);
                        log.debug(ADDED_URL, link);
                    });

        } catch (IOException e) {
            log.error(FAILED_CONNECTION, url, e);
            throw new FailedConnectionException(url);
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
