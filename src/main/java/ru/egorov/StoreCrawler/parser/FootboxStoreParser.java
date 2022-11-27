package ru.egorov.StoreCrawler.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.model.FootboxProduct;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class FootboxStoreParser extends StoreParser {
    private static final Logger log = LoggerFactory.getLogger(FootboxStoreParser.class);

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add("https://www.footboxshop.ru/catalog/obuv/");
        urls.add("https://www.footboxshop.ru/catalog/odezhda/");
        urls.add("https://www.footboxshop.ru/catalog/aksessuary/");
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, "pagination__item");
        addLinks(urls, "pagination__item", "PAGEN", 3);
        addLinks(urls, "catalog-card__model");
    }

    @Override
    public Store getStore() {
        return Store.FOOTBOX;
    }

    @Override
    public FootboxProduct parseProduct(String url) {
        log.debug("Parsing starts for url: \"{}\"", url);

        try {
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            if (doc.getElementsByClass("item-container").isEmpty()
                    || doc.getElementsByClass("not-avail").first()
                    .attr("style").equals("display: flex;")) {
                return null;
            }

            FootboxProduct product = new FootboxProduct();

            enrichProduct(product, doc);

            log.debug("Item with name \"{}\" was parsed", product.getName());

            return product;

        } catch (IOException e) {
            log.error("Failed connection: {}", e.getMessage());
            return null;
        }
    }

    private void enrichProduct(FootboxProduct product, Document doc) {
        product.setName(parseName(doc));

        product.setSku(parseSku(doc));

        product.setPrice(parsePrice(doc));

        product.setPriceCurrency(parseFromItemprop(doc, "priceCurrency"));

        product.setCategory(parseFromItemprop(doc, "category"));

        product.setImage(parseImage(doc));

        product.setSizes(parseSizes(doc));

        setColor(product, doc);

        setOtherProperties(product, doc);
    }

    private String parseName(Document doc) {
        String name = doc.getElementsByClass("item-info__title bx-title").first().text();

        return name.substring(name.indexOf(' ') + 1);
    }

    private String parseSku(Document doc) {
        return doc.getElementsByClass("item-info__art")
                .first()
                .text()
                .substring(9);
    }

    private Double parsePrice(Document doc) {
        return doc.getElementsByAttributeValue("itemprop", "price").stream()
                .map(element -> element.attr("content"))
                .filter(price -> !price.isBlank())
                .findFirst()
                .map(Double::parseDouble)
                .get();
    }

    private String parseImage(Document doc) {
        return "https://footboxshop.ru" + doc.getElementsByAttributeValue("itemprop", "image")
                .get(0)
                .attr("src");
    }

    private String parseSizes(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> sizesFromHtml = doc.getElementsByClass("sizes-public-detail__list__item")
                .eachAttr("title");

        if (sizesFromHtml.isEmpty()) {
            sizesFromHtml = doc.getElementsByClass("_item-sizes__item")
                    .eachAttr("title");
        }

        sizesFromHtml.forEach(size -> stringBuilder.append(", ")
                        .append(size));

        String sizes = stringBuilder.toString();

        if (sizes.startsWith(", ")) {
            sizes = sizes.substring(2);
        }

        return sizes;
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
        Elements propertyValue = doc.getElementsByClass("product-item-detail-properties")
                .select("dd");

        for (int i = 0; i < propertyNames.size(); i++) {
            switch (propertyNames.get(i).text()) {
                case ("Пол"):
                    product.setGender(propertyValue.get(i).text());
                    break;
                case ("Бренд"):
                    product.setBrand(propertyValue.get(i).text());
                    break;
                case ("Страна"):
                    product.setCountry(propertyValue.get(i).text());
                    break;
                case ("Состав"):
                    product.setComposition(propertyValue.get(i).text());
                    break;
                case ("Расцветка"):
                    product.setColoring(propertyValue.get(i).text());
                    break;
            }
        }
    }
}
