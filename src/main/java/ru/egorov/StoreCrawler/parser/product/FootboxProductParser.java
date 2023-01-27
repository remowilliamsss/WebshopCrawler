package ru.egorov.StoreCrawler.parser.product;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.model.FootboxProduct;
import ru.egorov.StoreCrawler.model.StoreType;
import ru.egorov.StoreCrawler.parser.FootboxStoreParser;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class FootboxProductParser extends ProductParser {

    public FootboxProductParser(FootboxStoreParser storeParser) {
        super(storeParser);
    }

    @Override
    public StoreType getStore() {
        return StoreType.footbox;
    }

    @Override
    public FootboxProduct parseProduct(String url) {
        log.debug("Parsing starts for url: \"{}\"", url);

        try {
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            //  TODO: И подумай насчет вынесения литералов в константы

            if (!isAvailableItem(doc)) {
                return null;
            }

            FootboxProduct product = new FootboxProduct();

            buildProduct(product, doc);

            product.setUrl(url);

            log.debug("Item with name \"{}\" was parsed", product.getName());

            return product;

        } catch (NoSuchElementException e) {
            log.error("Failed connection to {}: {}", url, e.getMessage());
            return null;
        } catch (IOException e) {
            // TODO: 13.12.2022 обрати внимание на log.error(String var1, Throwable var2)
            log.error("Failed connection to {}: {}", url, e.getMessage());
            // TODO: 13.12.2022 я бы вынес return из catch.
            //  Насколько вообще корректно скрывать ошибку,
            //  а не выбрасывать какой-нить RuntimeException в таком случае?
            return null;
        }
    }

    private boolean isAvailableItem(Document doc) {
        return !doc.getElementsByClass("item-container")
                .isEmpty()
                && !doc.getElementsByClass("not-avail")
                .first()
                .attr("style")
                .equals("display: flex;");
    }

    private void buildProduct(FootboxProduct product, Document doc) {
        product.setName(parseName(doc));
        product.setSku(parseSku(doc));
        product.setPrice(parsePrice(doc));
        product.setPriceCurrency(parseFromItemprop(doc, "priceCurrency"));
        product.setCategory(parseFromItemprop(doc, "category"));
        product.setImage(parseImage(doc));
        product.setSize(parseSizeRange(doc));

        setColor(product, doc);
        setOtherProperties(product, doc);
    }

    private String parseName(Document doc) {
        String name = doc.getElementsByClass("item-info__title bx-title")
                .first()
                .text();

        // TODO: 13.12.2022 извращение:)
        return name.substring(name.indexOf(' ') + 1);
    }

    private String parseSku(Document doc) {
        return doc.getElementsByClass("item-info__art")
                .first()
                .text()
                .substring(9);
    }

    private Double parsePrice(Document doc) {
        // TODO: 13.12.2022 .stream() на новой строке
        return doc.getElementsByAttributeValue("itemprop", "price")
                .stream()
                .map(element -> element.attr("content"))
                .filter(price -> !price.isBlank())
                .findFirst()
                .map(Double::parseDouble)
                .orElseThrow();
    }

    private String parseImage(Document doc) {

        // TODO: 13.12.2022 явный вызов get() - зло. Посмотри в сторону String.format() вместо конкатенации
        /*return "https://footboxshop.ru" + doc.getElementsByClass("item-slider__main-image").get(0)
                .attr("src");*/

        return doc.getElementsByClass("item-slider__main-image")
                .stream()
                .map(element -> element.attr("src"))
                .map(src -> "https://footboxshop.ru" + src)
                .findFirst()
                .orElseThrow();
    }

    private String parseSizeRange(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> sizeFromHtml = doc.getElementsByClass("sizes-public-detail__list__item")
                .eachAttr("title");

        if (sizeFromHtml.isEmpty()) {
            sizeFromHtml = doc.getElementsByClass("_item-sizes__item")
                    .eachAttr("title");
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
        Element color = doc.getElementById("colorValue");

        if (color != null) {
            product.setColor(color.text());
        }
    }

    private void setOtherProperties(FootboxProduct product, Document doc) {
        Elements propertyNames = doc.getElementsByClass("product-item-detail-properties")
                .select("dt");
        Elements propertyValues = doc.getElementsByClass("product-item-detail-properties")
                .select("dd");

        for (int i = 0; i < propertyNames.size(); i++) {
            setProperty(propertyNames.get(i), propertyValues.get(i), product);
        }
    }

    private void setProperty(Element name, Element value, FootboxProduct product) {
        switch (name.text()) {
            // TODO: 13.12.2022 значения кейсов в константы, в то и в поле элементов енама
            case "Пол" -> product.setGender(value.text());
            case "Бренд" -> product.setBrand(value.text());
            case "Страна"-> product.setCountry(value.text());
            case "Состав" -> product.setComposition(value.text());
            case "Расцветка" -> product.setColoring(value.text());
        }
    }
}
