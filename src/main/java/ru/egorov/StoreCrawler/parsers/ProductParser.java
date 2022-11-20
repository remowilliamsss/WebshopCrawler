package ru.egorov.StoreCrawler.parsers;

import ru.egorov.StoreCrawler.models.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO: 20.11.2022 Не могу понять, почему интерфейс?
public interface ProductParser {

    /*    Принимает url-адрес, возвращает объект класса Product, созданный на основе информации,
полученной с html-страницы по этому адресу.*/
    Product parseProduct(String url) throws IOException;

    /*    Принимает список url-адресов, возвращает список объектов класса Product,
созданных на основе информации, полученной с html-страниц по этим адресам.
Параметр isStopped для прерывания выполнения метода извне.*/
    // TODO: 20.11.2022 зачем вообще что-то делать до проверки isStopped?
    default List<Product> parseProducts (List<String> urls, Boolean isStopped) throws IOException {
        // TODO: 20.11.2022 В качестве примера, как переписал бы я. Только IOException картинку портит
//        if (isStopped) {
//            return new ArrayList<>();
//        }
//
//        return urls.stream()
//                .map(this::parseProduct)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());

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
