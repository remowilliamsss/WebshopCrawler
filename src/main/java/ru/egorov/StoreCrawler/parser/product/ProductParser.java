package ru.egorov.StoreCrawler.parser.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.egorov.StoreCrawler.model.Product;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.parser.StoreParser;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class ProductParser {
    private final StoreParser storeParser;

    public List<Product> parseProducts() {
        log.info("{} scanning starts", getStore());

        List<Product> products = parseProducts(storeParser.parsePages());

        log.info("{} scanning finished", getStore());

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
        return doc.getElementsByAttributeValue("itemprop", itemprop)
                .get(0)
                .attr("content");
    }

}
