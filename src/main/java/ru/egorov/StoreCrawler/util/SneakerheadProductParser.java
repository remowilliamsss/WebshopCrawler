package ru.egorov.StoreCrawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SneakerheadProductParser {

    /*    Принимает url-адрес, возвращает объект класса Product, созданный на основе информации,
    полученной с html-страницы по этому адресу.*/
    public SneakerheadProduct parseProduct(String url) throws IOException {
        Connection connection = Jsoup.connect(url).timeout(10000);
        Document doc = connection.get();
        Element element = doc.getElementById("neatsy-widget");

        if (element == null)
            return null;

        SneakerheadProduct product = new SneakerheadProduct();

        product.setName(element.attr("data-name"));
        product.setSku(element.attr("data-sku"));
        product.setCategory(element.attr("data-category"));
        product.setBrand(element.attr("data-brand"));
        product.setGender(element.attr("data-gender"));

        product.setImage(doc.getElementsByAttributeValueContaining("itemprop", "image").get(0)
                .attr("content"));
        product.setColor(doc.getElementsByAttributeValueContaining("itemprop", "color").get(0)
                .attr("content"));
        product.setPrice(Double.parseDouble(doc.getElementsByAttributeValueContaining("itemprop", "price")
                .get(0).attr("content")));
        product.setPriceCurrency(doc.getElementsByAttributeValueContaining("itemprop", "priceCurrency")
                .get(0).attr("content"));

        Elements elements = doc.getElementsByAttributeValueContaining("itemprop", "additionalProperty");
        for (Element el : elements) {
            if (el.select("[itemprop=\"name\"]").text().equals("Страна")) {
                product.setCountry(el.select("[itemprop=\"value\"]").text());
                break;
            }
        }

        elements = doc.getElementsByClass("product-sizes__list is-visible");

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

        System.out.println(product.getName());

        return product;
    }

    /*    Принимает список url-адресов, возвращает список объектов класса Product,
    созданных на основе информации, полученной с html-страниц по этим адресам.*/
    public List<SneakerheadProduct> parseProducts (List<String> urls) throws IOException {
        List<SneakerheadProduct> products = new ArrayList<>();

        for (String url : urls) {
            products.add(parseProduct(url));
        }

        products.removeIf(Objects::isNull);

        return products;
    }
}
