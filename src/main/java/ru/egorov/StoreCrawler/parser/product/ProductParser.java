package ru.egorov.StoreCrawler.parser.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.parser.store.StoreParser;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class ProductParser {
    private final StoreParser storeParser;

    public static final String CONTENT = "content";
    public static final String ITEMPROP = "itemprop";
    public static final String SCAN_FINISH = "{} scanning finished.";
    public static final String SCAN_START = "{} scanning is starting.";
    public static final String PARSE_START = "Parsing is starting for url: {}.";
    public static final String PARSE_FINISH = "Item with name \"{}\" was parsed.";
    public static final String PRICE_CURRENCY = "priceCurrency";
    public static final String CATEGORY = "category";
    public static final String PRICE = "price";
    public static final String COUNTRY = "Страна";
    public static final String GENDER = "Пол";

    public List<Product> parseProducts() {
        log.info(SCAN_START, getStore());

        List<Product> products = parseProducts(storeParser.parsePages());

        log.info(SCAN_FINISH, getStore());

        return products;
    }

    public List<Product> parseProducts (Collection<String> urls) {
        return urls.parallelStream()
                .map(this::parseProduct)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public abstract StoreType getStore();

    public abstract Product parseProduct(String url);

    protected String parseFromItemprop(Document doc, String itemprop) {
        return doc.getElementsByAttributeValue(ITEMPROP, itemprop)
                .stream()
                .findFirst()
                .map(element -> element.attr(CONTENT))
                .orElseThrow();
    }

}
