package ru.egorov.WebshopCrawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HtmlParser {

    /*    Метод принимает set и добавляет в него ссылки с html-страницы по адресу url,
    содержащиеся в элементах с именем класса elementClassName.*/
    public static void addLinks(String url, Set<String> set, String elementClassName) throws IOException {
        Connection connection = Jsoup.connect(url).timeout(10000);
        Document document = connection.get();
        Elements elements = document.getElementsByClass(elementClassName);

        for (Element element : elements.select("a[href]")) {
            String link = element.absUrl("href");
            set.add(link);
            System.out.println(link);
        }
    }

    /*    Метод принимает set с url-адресами, добавляет в него ссылки,
    содержащиеся в элементах класса elementClassName html-страниц по этим адресам.*/
    public static void addLinks(Set<String> set, String elementClassName) throws IOException {
        Set<String> setCopy = new HashSet<>(set);

        for (String url : setCopy) {
            addLinks(url, set, elementClassName);
        }
    }

    /*    Метод принимает set с url-адресами, добавляет в него ссылки, содержащиеся в элементах
    класса elementClassName html-страниц по адресам из set, которые содержат containedString.*/
    public static void addLinks(Set<String> set, String elementClassName, String containedString) throws IOException {
        Set<String> setCopy = new HashSet<>(set);

        for (String url : setCopy) {
            if (url.contains(containedString)) {
                addLinks(url, set, elementClassName);
            }
        }
    }
}
