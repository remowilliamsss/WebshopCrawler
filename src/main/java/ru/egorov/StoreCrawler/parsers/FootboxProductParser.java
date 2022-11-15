package ru.egorov.StoreCrawler.parsers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.egorov.StoreCrawler.models.FootboxProduct;

import java.io.IOException;
import java.util.List;

@Component
public class FootboxProductParser implements ProductParser {
    @Override
    public FootboxProduct parseProduct(String url) throws IOException {
        Connection connection = Jsoup.connect(url).timeout(10000);
        Document doc = connection.get();

        if (doc.getElementsByClass("item-container").isEmpty()
                || doc.getElementsByClass("not-avail").first().attr("style").equals("display: flex;"))
            return null;

        FootboxProduct product = new FootboxProduct();

        String name = doc.getElementsByClass("item-info__title bx-title").first().text();
        product.setName(name.substring(name.indexOf(' ') + 1));

        product.setSku(doc.getElementsByClass("item-info__art").first().text().substring(9));

        product.setCategory(doc.getElementsByAttributeValue("itemprop", "category").get(0)
                .attr("content"));
        product.setImage("https://footboxshop.ru"
                + doc.getElementsByAttributeValue("itemprop", "image").get(0)
                .attr("src"));
        product.setPriceCurrency(doc.getElementsByAttributeValue("itemprop", "priceCurrency").get(0)
                .attr("content"));

        Elements elements = doc.getElementsByAttributeValue("itemprop", "price");
        String price;

        for (Element element : elements) {
            price = element.attr("content");
            if (!price.isBlank()) {
                product.setPrice(Double.parseDouble(price));
                break;
            }
        }

        Element color = doc.getElementById("colorValue");

        if (color != null)
            product.setColor(color.text());

        Elements characteristicNames = doc.getElementsByClass("product-item-detail-properties")
                .select("dt");
        Elements characteristicValue = doc.getElementsByClass("product-item-detail-properties")
                .select("dd");

        for (int i = 0; i < characteristicNames.size(); i++) {
            switch (characteristicNames.get(i).text()) {
                case ("Пол"):
                    product.setGender(characteristicValue.get(i).text());
                    break;
                case ("Бренд"):
                    product.setBrand(characteristicValue.get(i).text());
                    break;
                case ("Страна"):
                    product.setCountry(characteristicValue.get(i).text());
                    break;
                case ("Состав"):
                    product.setComposition(characteristicValue.get(i).text());
                    break;
                case ("Расцветка"):
                    product.setColoring(characteristicValue.get(i).text());
                    break;
            }
        }

        List<String> sizesFromHtml = doc.getElementsByClass("sizes-public-detail__list__item").
                eachAttr("title");

        if (sizesFromHtml.isEmpty())
            sizesFromHtml = doc.getElementsByClass("_item-sizes__item").eachAttr("title");

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : sizesFromHtml) {
            stringBuilder.append(", ").append(s);
        }

        String sizes = stringBuilder.toString();

        if (sizes.startsWith(", "))
            sizes = sizes.substring(2);

        product.setSizes(sizes.toString());

        System.out.println(product.getName());

        return product;
    }
}
