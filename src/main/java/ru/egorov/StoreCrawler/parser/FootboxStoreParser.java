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
            // TODO: 13.12.2022 try-with-resource. Или хотя бы закрывай ресурс в finally.
            //  Создаешь утечки памяти на ровном месте
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document doc = connection.get();

            // TODO: 13.12.2022 условие if стоит вынести в приватный boolean-метод.
            //  И подумай насчет вынесения литералов в константы
            // TODO: 13.12.2022 при последовательном вызове методов используй правило "одна строчка - одна точка":
            //  doc.getElementsByClass("not-avail")
            //      .first()
            //      .attr("style")
            //      .equals("display: flex;")
            //      больше двух методов подряд в одной строке - нечитаемо
            if (doc.getElementsByClass("item-container").isEmpty()
                    || doc.getElementsByClass("not-avail").first()
                    .attr("style").equals("display: flex;")) {
                return null;
            }

            FootboxProduct product = new FootboxProduct();

            enrichProduct(product, doc);

            product.setUrl(url);

            log.debug("Item with name \"{}\" was parsed", product.getName());

            return product;

        } catch (IOException e) {
            // TODO: 13.12.2022 обрати внимание на log.error(String var1, Throwable var2)
            log.error("Failed connection to {}: {}", url, e.getMessage());
            // TODO: 13.12.2022 я бы вынес return из catch.
            //  Насколько вообще корректно скрывать ошибку,
            //  а не выбрасывать какой-нить RuntimeException в таком случае?
            return null;
        }
    }

    // TODO: 13.12.2022 мб лучше buildProduct()?
    private void enrichProduct(FootboxProduct product, Document doc) {
        // TODO: 13.12.2022 лишние пустые строки, исключая 101ю
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
        // TODO: 13.12.2022 строчка - точка
        String name = doc.getElementsByClass("item-info__title bx-title").first().text();

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
        return doc.getElementsByAttributeValue("itemprop", "price").stream()
                .map(element -> element.attr("content"))
                .filter(price -> !price.isBlank())
                .findFirst()
                .map(Double::parseDouble)
                // TODO: 13.12.2022 никогда не вызывай get() в явном виде
                .get();
    }

    private String parseImage(Document doc) {
        // TODO: 13.12.2022 явный вызов get() - зло. Посмотри в сторону String.format() вместо конкатенации
        return "https://footboxshop.ru" + doc.getElementsByClass("item-slider__main-image").get(0)
                .attr("src");
    }

    private String parseSizes(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();

        // TODO: 13.12.2022 неудачное название. Предлоги - плохая практика в большинстве случаев,
        //  сущ. во мн.ч. всегда последнее
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

        // TODO: 13.12.2022 в .forEach()
        for (int i = 0; i < propertyNames.size(); i++) {
            switch (propertyNames.get(i).text()) {
                // TODO: 13.12.2022 значения кейсов в константы, в то и в поле элементов енама
                case "Пол":// TODO: 13.12.2022 зачем скобки?
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
