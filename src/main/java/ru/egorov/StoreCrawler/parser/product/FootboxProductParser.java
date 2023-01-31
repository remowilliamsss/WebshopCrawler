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
import ru.egorov.StoreCrawler.service.FootboxProductService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class FootboxProductParser extends ProductParser {
    public static final String BRAND = "Бренд";
    public static final String COMPOSITION = "Состав";
    public static final String COLORING = "Расцветка";
    public static final String ART = "item-info__art";
    public static final String IMAGE = "item-slider__main-image";
    public static final String SIZES_1 = "sizes-public-detail__list__item";
    public static final String SIZES_2 = "_item-sizes__item";
    public static final String DETAIL_PROPERTIES = "product-item-detail-properties";
    public static final String ITEM_TITLE = "item-info__title bx-title";
    public static final String FLEX = "display: flex;";
    public static final String MAIN_PAGE = "https://footboxshop.ru%s";

    public FootboxProductParser(FootboxStoreParser storeParser, FootboxProductService productService) {
        super(storeParser, productService);
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

            FootboxProduct product = buildProduct(doc);

            product.setUrl(url);

            log.debug(PARSE_FINISH, product.getName());

            return product;

        } catch (IOException e) {
            log.error(StoreParser.FAILED_CONNECTION, url, e);
            throw new FailedConnectionException(url);
        }
    }

    private boolean isAvailableItem(Document doc) {
        return !doc.getElementsByClass("item-container")
                .isEmpty()
                && !doc.getElementsByClass("not-avail")
                .stream()
                .map(element -> element.attr("style")
                        .equals(FLEX))
                .findFirst()
                .orElseThrow();
    }

    private FootboxProduct buildProduct(Document doc) {
        FootboxProduct product = new FootboxProduct();

        product.setName(parseName(doc));
        product.setSku(parseSku(doc));
        product.setPrice(parsePrice(doc));
        product.setImage(parseImage(doc));
        product.setSize(parseSize(doc));
        product.setPriceCurrency(parseFromItemprop(doc, PRICE_CURRENCY));
        product.setCategory(parseFromItemprop(doc, CATEGORY));

        setColor(product, doc);
        setOther(product, doc);

        return product;
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
                .map(element -> element.attr("src"))
                .map(src -> String.format(MAIN_PAGE, src))
                .findFirst()
                .orElseThrow();
    }

    private String parseSize(Document doc) {
        List<String> sizeFromHtml = parseSizeFromHtml(doc);

        return String.join(COMMA, sizeFromHtml);
    }

    private List<String> parseSizeFromHtml(Document doc) {
        List<String> sizeFromHtml = parseSizeFromHtml(doc, SIZES_1);

        if (sizeFromHtml.isEmpty()) {
            sizeFromHtml = parseSizeFromHtml(doc, SIZES_2);
        }

        return sizeFromHtml;
    }

    private List<String> parseSizeFromHtml(Document doc, String attrName) {
        return doc.getElementsByClass(attrName)
                .eachAttr("title");
    }

    private void setColor(FootboxProduct product, Document doc) {
        Element color = doc.getElementById("colorValue");

        if (color != null) {
            product.setColor(color.text());
        }
    }

    private void setOther(FootboxProduct product, Document doc) {
        Elements propertyNames = doc.getElementsByClass(DETAIL_PROPERTIES)
                .select("dt");
        Elements propertyValues = doc.getElementsByClass(DETAIL_PROPERTIES)
                .select("dd");

        for (int i = 0; i < propertyNames.size(); i++) {
            setProperty(product, propertyNames.get(i), propertyValues.get(i));
        }
    }

    private void setProperty(FootboxProduct product, Element name, Element value) {
        switch (name.text()) {
            case GENDER -> product.setGender(value.text());
            case BRAND -> product.setBrand(value.text());
            case COUNTRY -> product.setCountry(value.text());
            case COMPOSITION -> product.setComposition(value.text());
            case COLORING -> product.setColoring(value.text());
        }
    }
}
