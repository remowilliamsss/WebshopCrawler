package ru.egorov.StoreCrawler.parser.product;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.exception.FailedConnectionException;
import ru.egorov.StoreCrawler.model.SneakerheadProduct;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.parser.store.SneakerheadStoreParser;
import ru.egorov.StoreCrawler.parser.store.StoreParser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SneakerheadProductParser extends ProductParser {
    public static final String SKU = "sku";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String COLOR = "color";
    public static final String BRAND = "brand";
    public static final String PRODUCT = "product";
    public static final String WITH_SPACE = "%s %s";
    public static final String CLASS = "[class=\"%s\"]";
    public static final String DATA_NAME = "data-name";
    public static final String DATA_SIZE = "data-size-chart-name";
    public static final String ITEMPROP_NAME = "[itemprop=\"name\"]";
    public static final String ITEMPROP_VALUE = "[itemprop=\"value\"]";
    public static final String SIZES = "product-sizes__list is-visible";
    public static final String ADDITIONAL_PROPERTY = "additionalProperty";
    public static final String SIZES_BUTTON_2 = "product-sizes__button styled-button styled-button--default";
    public static final String SIZES_BUTTON_1 = "product-sizes__button styled-button styled-button--default is-active";

    public SneakerheadProductParser(SneakerheadStoreParser storeParser) {
        super(storeParser);
    }

    @Override
    public StoreType getStore() {
        return StoreType.sneakerhead;
    }

    @Override
    public SneakerheadProduct parseProduct(String url) {
        log.debug(PARSE_START, url);

        try {
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            if (!isAvailableItem(doc)) {
                return null;
            }

            SneakerheadProduct product = buildProduct(doc);

            product.setUrl(url);

            log.debug(PARSE_FINISH, product.getName());

            return product;

        } catch (IOException e) {
            log.error(StoreParser.FAILED_CONNECTION, url, e);
            throw new FailedConnectionException(url);
        }
    }

    private boolean isAvailableItem(Document doc) {
        return !doc.getElementsByClass(PRODUCT)
                .isEmpty()
                && !parseFromItemprop(doc, PRICE).isBlank();
    }

    private SneakerheadProduct buildProduct(Document doc) {
        SneakerheadProduct product = new SneakerheadProduct();

        product.setSku(parseFromItemprop(doc, SKU));
        product.setPrice(parsePrice(doc));
        product.setPriceCurrency(parseFromItemprop(doc, PRICE_CURRENCY));
        product.setCategory(parseFromItemprop(doc, CATEGORY));
        product.setImage(parseFromItemprop(doc, IMAGE));
        product.setColor(parseFromItemprop(doc, COLOR));
        product.setBrand(parseFromItemprop(doc, BRAND));
        product.setName(parseName(doc, product.getBrand()));
        product.setCountry(parseAdditionalProperty(doc, COUNTRY));
        product.setGender(parseAdditionalProperty(doc, GENDER));
        product.setSize(parseSize(doc));

        return product;
    }

    private String parseName(Document doc, String brand) {
        return doc.getElementsByAttributeValue(ITEMPROP, NAME)
                .stream()
                .filter(element -> element.hasAttr(CONTENT))
                .map(element -> element.attr(CONTENT)
                        .trim())
                .map(name -> String.format(WITH_SPACE, brand, name)
                        .trim())
                .findFirst()
                .orElseThrow();
    }

    private Double parsePrice(Document doc) {
        return Double.parseDouble(parseFromItemprop(doc, PRICE));
    }

    private String parseSize(Document doc) {
        Elements elements = doc.getElementsByClass(SIZES);
        String sizeCountry = elements.attr(DATA_SIZE);

        return parseSizeFromHtml(elements)
                .stream()
                .map(size -> addSizeCountry(size, sizeCountry))
                .collect(Collectors.joining(COMMA));
    }

    private List<String> parseSizeFromHtml(Elements elements) {
        List<String> sizesFromHtml = parseSizeFromHtml(elements, SIZES_BUTTON_1);

        sizesFromHtml.addAll(parseSizeFromHtml(elements, SIZES_BUTTON_2));

        return sizesFromHtml;
    }

    private List<String> parseSizeFromHtml(Elements elements, String className) {
        return elements.select(String.format(CLASS, className))
                .eachAttr(DATA_NAME);
    }

    private String addSizeCountry(String size, String sizeCountry) {
        if (sizeCountry.isBlank()) {
            return size;
        } else {
            return String.format(WITH_SPACE, size, sizeCountry);
        }
    }

    private String parseAdditionalProperty(Document doc, String propertyName) {
        return doc.getElementsByAttributeValue(ITEMPROP, ADDITIONAL_PROPERTY)
                .stream()
                .filter(element -> element.select(ITEMPROP_NAME)
                        .text()
                        .equals(propertyName))
                .map(element -> element.select(ITEMPROP_VALUE)
                        .text())
                .findFirst()
                .orElse(null);
    }
}
