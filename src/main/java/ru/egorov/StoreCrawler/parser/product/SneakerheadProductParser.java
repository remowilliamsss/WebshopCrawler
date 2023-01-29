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

@Slf4j
@Component
public class SneakerheadProductParser extends ProductParser {
    public static final String SKU = "sku";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String COLOR = "color";
    public static final String BRAND = "brand";
    public static final String PRODUCT = "product";
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

            if (doc.getElementsByClass(PRODUCT).isEmpty()) {
                return null;
            }

            SneakerheadProduct product = new SneakerheadProduct();

            buildProduct(product, doc);

            product.setUrl(url);

            log.debug(PARSE_FINISH, product.getName());

            return product;

        } catch (IOException e) {
            log.error(StoreParser.FAILED_CONNECTION, url, e);
            throw new FailedConnectionException(url);
        }
    }

    private void buildProduct(SneakerheadProduct product, Document doc) {
        product.setSku(parseFromItemprop(doc, SKU));
        product.setCategory(parseFromItemprop(doc, CATEGORY));
        product.setImage(parseFromItemprop(doc, IMAGE));
        product.setColor(parseFromItemprop(doc, COLOR));
        product.setBrand(parseFromItemprop(doc, BRAND));

        setName(product, product.getBrand(), doc);
        setPrice(product, doc);
        setCountry(product, doc);
        setGender(product, doc);
        setSize(product, doc);
    }

    private void setName(SneakerheadProduct product, String brand, Document doc) {
        product.setName(doc.getElementsByAttributeValue(ITEMPROP, NAME)
                .stream()
                .filter(element -> element.hasAttr(CONTENT))
                .map(element -> element.attr(CONTENT)
                        .trim())
                .map(name -> String.format("%s %s", brand, name))
                .findFirst()
                .orElseThrow());
    }

    private void setPrice(SneakerheadProduct product, Document doc) {
        String price = parseFromItemprop(doc, PRICE);

        if (!price.isBlank()) {
            product.setPrice(Double.parseDouble(price));
            product.setPriceCurrency(parseFromItemprop(doc, PRICE_CURRENCY));
        }
    }
    private void setCountry(SneakerheadProduct product, Document doc) {
        String country = parseAdditionalProperty(doc, COUNTRY);

        if (country != null) {
            product.setCountry(country);
        }
    }

    private void setGender(SneakerheadProduct product, Document doc) {
        String gender = parseAdditionalProperty(doc, GENDER);

        if (gender != null) {
            product.setGender(gender);
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

    private void setSize(SneakerheadProduct product, Document doc) {
        Elements elements = doc.getElementsByClass(SIZES);

        if (!elements.isEmpty()) {
            List<String> sizesFromHtml = getSizeFromHtml(elements);

            StringBuilder size = new StringBuilder(sizesFromHtml.get(0));

            addSizeCountry(size, elements);

            for (int i = 1; i < sizesFromHtml.size(); i++) {
                addSize(size, sizesFromHtml.get(i), elements);
            }

            product.setSize(size.toString());
        }
    }

    private List<String> getSizeFromHtml(Elements elements) {
        List<String> sizesFromHtml = getSizeFromHtml(elements, SIZES_BUTTON_1);

        sizesFromHtml.addAll(getSizeFromHtml(elements, SIZES_BUTTON_2));

        return sizesFromHtml;
    }

    private List<String> getSizeFromHtml(Elements elements, String className) {
        return elements.select(String.format(CLASS, className))
                .eachAttr(DATA_NAME);
    }

    private void addSize(StringBuilder line, String size, Elements elements) {
        line.append(", ")
                .append(size);

        addSizeCountry(line, elements);
    }

    private void addSizeCountry(StringBuilder line, Elements elements) {
        if (!elements.attr(DATA_SIZE)
                .isBlank()) {

            line.append(" ")
                    .append(elements.attr(DATA_SIZE));
        }
    }
}
