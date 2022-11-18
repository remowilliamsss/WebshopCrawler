package ru.egorov.StoreCrawler.parsers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;

import java.io.IOException;
import java.util.List;

@Component
public class SneakerheadProductParser implements ProductParser {

    @Override
    public SneakerheadProduct parseProduct(String url) throws IOException {
        Connection connection = Jsoup.connect(url).timeout(20000);
        Document doc = connection.get();

        if (doc.getElementsByClass("product").isEmpty())
            return null;

        SneakerheadProduct product = new SneakerheadProduct();

        product.setSku(doc.getElementsByAttributeValue("itemprop", "sku").get(0)
                .attr("content"));
        product.setCategory(doc.getElementsByAttributeValue("itemprop", "category").get(0)
                .attr("content"));
        product.setBrand(doc.getElementsByAttributeValue("itemprop", "brand").get(0)
                .attr("content"));
        product.setImage(doc.getElementsByAttributeValue("itemprop", "image").get(0)
                .attr("content"));
        product.setColor(doc.getElementsByAttributeValue("itemprop", "color").get(0)
                .attr("content"));

        for (Element element : doc.getElementsByAttributeValue("itemprop", "name")) {
            if (element.hasAttr("content")) {
                product.setName(product.getBrand() + " " + element.attr("content").trim());
                break;
            }
        }

        if (!doc.getElementsByAttributeValue("itemprop", "price").get(0).attr("content").isBlank()) {
            product.setPrice(Double.parseDouble(doc.getElementsByAttributeValue("itemprop", "price")
                    .get(0).attr("content")));
            product.setPriceCurrency(doc.getElementsByAttributeValue("itemprop", "priceCurrency")
                    .get(0).attr("content"));
        }

        Elements elements = doc.getElementsByAttributeValue("itemprop", "additionalProperty");

        for (Element el : elements) {
            if (el.select("[itemprop=\"name\"]").text().equals("Страна")) {
                product.setCountry(el.select("[itemprop=\"value\"]").text());
                break;
            }
        }

        for (Element el : elements) {
            if (el.select("[itemprop=\"name\"]").text().equals("Пол")) {
                product.setGender(el.select("[itemprop=\"value\"]").text());
                break;
            }
        }

        elements = doc.getElementsByClass("product-sizes__list is-visible");

        if (!elements.isEmpty()) {
            Elements innerElements = elements.select(
                    "[class=\"product-sizes__button styled-button styled-button--default is-active\"]");
            List<String> sizesFromHtml = innerElements.eachAttr("data-name");

            innerElements = elements.select(
                    "[class=\"product-sizes__button styled-button styled-button--default\"]");
            sizesFromHtml.addAll(innerElements.eachAttr("data-name"));

            StringBuilder sizes = new StringBuilder(sizesFromHtml.get(0));

            if (!elements.attr("data-size-chart-name").isBlank())
                sizes.append(" ").append(elements.attr("data-size-chart-name"));

            for (int i = 1; i < sizesFromHtml.size(); i++) {
                sizes.append(", ").append(sizesFromHtml.get(i));

                if (!elements.attr("data-size-chart-name").isBlank())
                    sizes.append(" ").append(elements.attr("data-size-chart-name"));
            }

            product.setSizes(sizes.toString());
        }

        System.out.println(product.getName());

        return product;
    }
}
