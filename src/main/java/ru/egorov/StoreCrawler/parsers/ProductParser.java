package ru.egorov.StoreCrawler.parsers;

import ru.egorov.StoreCrawler.models.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ProductParser {

    /*    Принимает url-адрес, возвращает объект класса Product, созданный на основе информации,
полученной с html-страницы по этому адресу.*/
    Product parseProduct(String url) throws IOException;

    /*    Принимает список url-адресов, возвращает список объектов класса Product,
созданных на основе информации, полученной с html-страниц по этим адресам.
Параметр isStopped для прерывания выполнения метода извне.*/
    default List<Product> parseProducts (List<String> urls, Boolean isStopped) throws IOException {
        List<Product> products = new ArrayList<>();

        for (String url : urls) {
            if (isStopped)
                return products;

            products.add(parseProduct(url));
        }

        products.removeIf(Objects::isNull);

        return products;
    }
}
