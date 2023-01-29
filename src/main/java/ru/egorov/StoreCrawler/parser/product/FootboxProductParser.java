package ru.egorov.StoreCrawler.parser.product;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.exception.FailedConnectionException;
import ru.egorov.StoreCrawler.model.FootboxProduct;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.parser.store.FootboxStoreParser;
import ru.egorov.StoreCrawler.parser.store.StoreParser;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class FootboxProductParser extends ProductParser {
    public static final String DT = "dt";
    public static final String DD = "dd";
    public static final String SRC = "src";
    public static final String BRAND = "Бренд";
    public static final String STYLE = "style";
    public static final String TITLE = "title";
    public static final String COLOR = "colorValue";
    public static final String ART = "item-info__art";
    public static final String COMPOSITION = "Состав";
    public static final String COLORING = "Расцветка";
    public static final String NOT_AVAIL = "not-avail";
    public static final String FLEX = "display: flex;";
    public static final String SIZES_2 = "_item-sizes__item";
    public static final String ITEM_CONTAINER = "item-container";
    public static final String IMAGE = "item-slider__main-image";
    public static final String MAIN_PAGE = "https://footboxshop.ru%s";
    public static final String ITEM_TITLE = "item-info__title bx-title";
    public static final String SIZES_1 = "sizes-public-detail__list__item";
    public static final String DETAIL_PROPERTIES = "product-item-detail-properties";

    public FootboxProductParser(FootboxStoreParser storeParser) {
        super(storeParser);
    }

    @Override
    public StoreType getStore() {
        return StoreType.footbox;
    }

    @Override
    public FootboxProduct parseProduct(String url) {
        log.debug(PARSE_START, url);

        try {
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            if (!isAvailableItem(doc)) {
                return null;
            }

            FootboxProduct product = new FootboxProduct();

            buildProduct(product, doc);

            product.setUrl(url);

            log.debug(PARSE_FINISH, product.getName());

            return product;

        } catch (IOException e) {
            log.error(StoreParser.FAILED_CONNECTION, url, e);
            throw new FailedConnectionException(url);
        }
    }

    private boolean isAvailableItem(Document doc) {
        return !doc.getElementsByClass(ITEM_CONTAINER)
                .isEmpty()
                && !doc.getElementsByClass(NOT_AVAIL)
                .stream()
                .map(element -> element.attr(STYLE)
                        .equals(FLEX))
                .findFirst()
                .orElseThrow();
    }

    private void buildProduct(FootboxProduct product, Document doc) {
        product.setName(parseName(doc));
        product.setSku(parseSku(doc));
        product.setPrice(parsePrice(doc));
        product.setPriceCurrency(parseFromItemprop(doc, PRICE_CURRENCY));
        product.setCategory(parseFromItemprop(doc, CATEGORY));
        product.setImage(parseImage(doc));
        product.setSize(parseSizeRange(doc));

        setColor(product, doc);
        setOther(product, doc);
    }

    private String parseName(Document doc) {
        return doc.getElementsByClass(ITEM_TITLE)
                .stream()
                .map(Element::text)
                .map(text -> text.substring(text.indexOf(' ') + 1))
                .findFirst()
                .orElseThrow();
    }

    private String parseSku(Document doc) {
        return doc.getElementsByClass(ART)
                .stream()
                .map(Element::text)
                .map(text -> text.substring(9))
                .findFirst()
                .orElseThrow();
    }

    private Double parsePrice(Document doc) {
        return doc.getElementsByAttributeValue(ITEMPROP, PRICE)
                .stream()
                .map(element -> element.attr(CONTENT))
                .filter(price -> !price.isBlank())
                .map(Double::parseDouble)
                .findFirst()
                .orElseThrow();
    }

    private String parseImage(Document doc) {
        return doc.getElementsByClass(IMAGE)
                .stream()
                .map(element -> element.attr(SRC))
                .map(src -> String.format(MAIN_PAGE, src))
                .findFirst()
                .orElseThrow();
    }

    private String parseSizeRange(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> sizeFromHtml = doc.getElementsByClass(SIZES_1)
                .eachAttr(TITLE);

        if (sizeFromHtml.isEmpty()) {
            sizeFromHtml = doc.getElementsByClass(SIZES_2)
                    .eachAttr(TITLE);
        }

        sizeFromHtml.forEach(size -> stringBuilder.append(", ")
                .append(size));

        String size = stringBuilder.toString();

        if (size.startsWith(", ")) {
            size = size.substring(2);
        }

        return size;
    }

    private void setColor(FootboxProduct product, Document doc) {
        Element color = doc.getElementById(COLOR);

        if (color != null) {
            product.setColor(color.text());
        }
    }

    private void setOther(FootboxProduct product, Document doc) {
        Elements propertyNames = doc.getElementsByClass(DETAIL_PROPERTIES)
                .select(DT);
        Elements propertyValues = doc.getElementsByClass(DETAIL_PROPERTIES)
                .select(DD);

        for (int i = 0; i < propertyNames.size(); i++) {
            setProperty(propertyNames.get(i), propertyValues.get(i), product);
        }
    }

    private void setProperty(Element name, Element value, FootboxProduct product) {
        switch (name.text()) {
            case GENDER -> product.setGender(value.text());
            case BRAND -> product.setBrand(value.text());
            case COUNTRY -> product.setCountry(value.text());
            case COMPOSITION -> product.setComposition(value.text());
            case COLORING -> product.setColoring(value.text());
        }
    }
}
