package ru.egorov.storecrawler.service.parser.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.egorov.storecrawler.model.Product;
import ru.egorov.storecrawler.model.StoreType;
import ru.egorov.storecrawler.service.parser.store.StoreParserService;
import ru.egorov.storecrawler.service.product.ProductService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class ProductParserService {
    public static final String PARSE_START = "Parsing is starting for url: {}.";
    public static final String PARSE_FINISH = "Item with name \"{}\" was parsed.";
    public static final String CONTENT = "content";
    public static final String ITEMPROP = "itemprop";
    public static final String CATEGORY = "category";
    public static final String PRICE = "price";
    public static final String PRICE_CURRENCY = "priceCurrency";
    public static final String GENDER = "Пол";
    public static final String COUNTRY = "Страна";
    public static final String COMMA = ", ";

    private final StoreParserService storeParser;
    private final ProductService productService;

    public void parseAll() {
        log.info("{} scanning is starting.", getStore());

        List<Product> products = parseAll(storeParser.parse());

        productService.updateAll(products);

        log.info("{} scanning finished.", getStore());
    }

    public List<Product> parseAll(Collection<String> urls) {
        return urls.parallelStream()
                .map(this::parse)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public abstract StoreType getStore();

    public abstract Product parse(String url);

    protected String parseFromItemprop(Document doc, String itemprop) {
        return doc.getElementsByAttributeValue(ITEMPROP, itemprop)
                .stream()
                .map(element -> element.attr(CONTENT))
                .findFirst()
                .orElseThrow();
    }

}
