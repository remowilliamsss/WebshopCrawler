package ru.egorov.StoreCrawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
// TODO: 20.11.2022 Если компонент, значит не util. Утилитные классы -
//  классы с статик-методами под минорные задачи, не связанные с бизнес-логикой
public class HtmlParser {

    /*    Метод принимает set и добавляет в него ссылки с html-страницы по адресу url,
    содержащиеся в элементах с именем класса elementClassName.
    Параметр isStopped для прерывания выполнения метода извне.*/
    public void addLinks(String url, Set<String> set, String elementClassName, Boolean isStopped) throws IOException {
        Connection connection = Jsoup.connect(url).timeout(10000);
        Document document = connection.get();
        Elements elements = document.getElementsByClass(elementClassName);

        for (Element element : elements.select("a[href]")) {
            if (isStopped)
                return;

            String link = element.absUrl("href");
            set.add(link);
            System.out.println(link);
        }
    }

    /*    Метод принимает set с url-адресами, добавляет в него ссылки,
    содержащиеся в элементах класса elementClassName html-страниц по этим адресам.
    Параметр isStopped для прерывания выполнения метода извне.*/
    public void addLinks(Set<String> set, String elementClassName, Boolean isStopped) throws IOException {
        Set<String> setCopy = new HashSet<>(set);

        for (String url : setCopy) {
            if (isStopped)
                return;

            addLinks(url, set, elementClassName, isStopped);
        }
    }

    /*    Метод принимает set с url-адресами, добавляет в него ссылки, содержащиеся в элементах
    класса elementClassName html-страниц по адресам из set, которые содержат containedString.
    Параметр isStopped для прерывания выполнения метода извне.*/
    public void addLinks(Set<String> set, String elementClassName,
                         String containedString, Boolean isStopped) throws IOException {
        Set<String> setCopy = new HashSet<>(set);

        for (String url : setCopy) {
            if (isStopped)
                return;

            if (url.contains(containedString))
                addLinks(url, set, elementClassName, isStopped);
        }
    }

    /*    Метод принимает set с url-адресами, добавляет в него ссылки, содержащиеся в элементах
    класса elementClassName html-страниц по адресам из set, которые содержат containedString.
    Параметр isStopped для прерывания выполнения метода извне.
    Операция повторяется равное times количество раз.*/
    public void addLinks(Set<String> set, String elementClassName,
                         String containedString, Boolean isStopped, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            if (isStopped)
                return;

            addLinks(set, elementClassName, containedString, isStopped);
        }
    }
}
