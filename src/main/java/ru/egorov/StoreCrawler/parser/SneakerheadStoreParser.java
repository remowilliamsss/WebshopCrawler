package ru.egorov.StoreCrawler.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.Store;
import ru.egorov.StoreCrawler.model.SneakerheadProduct;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class SneakerheadStoreParser extends StoreParser {
    private static final Logger log = LoggerFactory.getLogger(SneakerheadStoreParser.class);

    @Override
    protected void addCategories(Set<String> urls) {
        urls.add("https://sneakerhead.ru/shoes/");
        urls.add("https://sneakerhead.ru/clothes/");
        urls.add("https://sneakerhead.ru/stuff/");
        urls.add("https://sneakerhead.ru/kiosk/");
    }

    @Override
    protected void addItemPages(Set<String> urls) {
        addLinks(urls, "pagination");
        addLinks(urls, "pagination", "PAGEN");
        addLinks(urls, "product-cards__item");
    }

    @Override
    public Store getStore() {
        return Store.SNEAKERHEAD;
    }

    @Override
    public SneakerheadProduct parseProduct(String url) {
        log.debug("Parsing starts for url: \"{}\"", url);

        try {
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            if (doc.getElementsByClass("product").isEmpty()) {
                return null;
            }

            SneakerheadProduct product = new SneakerheadProduct();

            enrichProduct(product, doc);

            product.setUrl(url);

            log.debug("Item with name \"{}\" was parsed", product.getName());

            return product;

        } catch (IOException e) {
            log.error("Failed connection to {}: {}", url, e.getMessage());
            return null;
        } catch (NoSuchElementException e) {
            log.error("Failed to parse name: {}", e.getMessage());
            return null;
        }
    }

    private void enrichProduct(SneakerheadProduct product, Document doc) {
        product.setSku(parseFromItemprop(doc, "sku"));

        product.setCategory(parseFromItemprop(doc, "category"));

        product.setImage(parseFromItemprop(doc, "image"));

        product.setColor(parseFromItemprop(doc, "color"));

        product.setBrand(parseFromItemprop(doc, "brand"));

        setName(product, product.getBrand(), doc);

        setPrice(product, doc);

        setCountry(product, doc);

        setGender(product, doc);

        setSizes(product, doc);
    }

    private void setName(SneakerheadProduct product, String brand, Document doc) {
        product.setName(doc.getElementsByAttributeValue("itemprop", "name")
                .stream()
                .filter(element -> element.hasAttr("content"))
                .findFirst()
                .map(element -> brand + " " + element.attr("content").trim())
                .orElseThrow());
    }

    private void setPrice(SneakerheadProduct product, Document doc) {
        String price = parseFromItemprop(doc, "price");

        if (!price.isBlank()) {
            product.setPrice(Double.parseDouble(price));
            product.setPriceCurrency(parseFromItemprop(doc, "priceCurrency"));
        }
    }
    private void setCountry(SneakerheadProduct product, Document doc) {
        String country = parseAdditionalProperty(doc, "Страна");

        if (country != null) {
            product.setCountry(country);
        }
    }

    private void setGender(SneakerheadProduct product, Document doc) {
        String gender = parseAdditionalProperty(doc, "Пол");

        if (gender != null) {
            product.setGender(gender);
        }
    }

    private String parseAdditionalProperty(Document doc, String propertyName) {
        return doc.getElementsByAttributeValue("itemprop", "additionalProperty")
                .stream()
                .filter(element -> element.select("[itemprop=\"name\"]").text().equals(propertyName))
                .findFirst()
                .map(element -> element.select("[itemprop=\"value\"]").text())
                .orElse(null);
    }

    private void setSizes(SneakerheadProduct product, Document doc) {
        Elements elements = doc.getElementsByClass("product-sizes__list is-visible");

        if (!elements.isEmpty()) {
            List<String> sizesFromHtml = getSizesFromHtml(elements);

            StringBuilder sizes = new StringBuilder(sizesFromHtml.get(0));

            addSizeCountry(sizes, elements);

            for (int i = 1; i < sizesFromHtml.size(); i++) {
                addSize(sizes, sizesFromHtml.get(i), elements);
            }

            product.setSizes(sizes.toString());
        }
    }

    private List<String> getSizesFromHtml(Elements elements) {
        List<String> sizesFromHtml = getSizesFromHtml(elements,
                "product-sizes__button styled-button styled-button--default is-active");

        sizesFromHtml.addAll(getSizesFromHtml(
                elements, "product-sizes__button styled-button styled-button--default"));

        return sizesFromHtml;
    }

    private List<String> getSizesFromHtml(Elements elements, String className) {
        return elements.select("[class=\"" + className + "\"]")
                .eachAttr("data-name");
    }

    private void addSize(StringBuilder line, String size, Elements elements) {
        line.append(", ").append(size);

        addSizeCountry(line, elements);
    }

    private void addSizeCountry(StringBuilder line, Elements elements) {
        if (!elements.attr("data-size-chart-name").isBlank()) {
            line.append(" ").append(elements.attr("data-size-chart-name"));
        }
    }
}
