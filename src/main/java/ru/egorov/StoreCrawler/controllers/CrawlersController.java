package ru.egorov.StoreCrawler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.crawlers.Crawler;
import ru.egorov.StoreCrawler.exceptions.StoreNotFoundException;
import ru.egorov.StoreCrawler.http.ErrorResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: 20.11.2022 пакеты принято называть в единственном числе
@RestController
// TODO: 20.11.2022 crawlers/ Я бы вообще рекомендовал начинать гкды с общего корня,
//  аля api/crawlers
@RequestMapping("crawlers")
public class CrawlersController {
    // TODO: 20.11.2022 не самая удачная затея тянуть в контроллер лист бинов.
    //  Лучше сделать класс, отвечающий за диспетчеризацию и выбор подходящего бина
    //  Паттерн стратегия, как один из вариантов
    private final List<Crawler> crawlers;

    @Autowired
    // TODO: 20.11.2022 советую познакомиться с ломбоком, сильно упрощает жизнь в плане
    //  создания геттеров, конструкторов и прочей рутинной хрени
    public CrawlersController(Crawler... crawlers) {
        this.crawlers = new ArrayList<>(List.of(crawlers));
    }

    @GetMapping("{storeName}/scan")
    // TODO: 20.11.2022 не рекомендую отдавать строку из эндпоинта. Либо дто,
    //  либо дто, обернутое в какой-нибудь ResponseEntity.
    //  Современный веб привык работать с json или, на худой конец, XML.
    //  Строка - точно неудачный выбор возвращаемого формата
    public String scan(@PathVariable("storeName") String storeName) {
        // TODO: 20.11.2022  для функционального стиля и не только советую взять за правило:
        //  одна точка - одна строчка. Конкретно в этом месте я подправлю:
        Optional<Crawler> foundCrawler = crawlers.stream()
                .filter(c -> c.getStoreName().equalsIgnoreCase(storeName))
                .findFirst();

//        TODO: если начали писать функционально - зачем вышли из функционлаьного стиля?
//         С 11 java есть метод Optional .ifPresentOrElse
        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            // TODO: 20.11.2022 почему не лямбда? Анонимные классы в таком виде
            //  - устаревший вариант синтаксиса
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        crawler.scan();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            return crawler.getStoreName() + " scan started";
        }

        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/stop_scan")
    public String stopScan(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        // TODO: 20.11.2022 можно логику из if было бы засунуть внутрь .map() у Optional
        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.stopScan();

            return crawler.getStoreName() + " scan stopped";
        }
        // TODO: 20.11.2022 а это - в .orElseThrow() после .map()
        //  Также советую познакомиться с @ControllerAdvice, в т.ч.
        //  как средством обработки ошибок в контроллерах
        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/start")
    public String start(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.start();

            return crawler.getStoreName() + " crawler started";
        }
        throw new StoreNotFoundException();
    }

    @GetMapping("{storeName}/stop")
    public String stop(@PathVariable("storeName") String storeName) {
        Optional<Crawler> foundCrawler = crawlers.stream().filter(c -> c.getStoreName()
                .equalsIgnoreCase(storeName)).findFirst();

        if (foundCrawler.isPresent()) {
            Crawler crawler = foundCrawler.get();

            crawler.stop();

            return crawler.getStoreName() + " crawler stopped";
        }
        throw new StoreNotFoundException();
    }

    @ExceptionHandler
    // TODO: 20.11.2022 направление мыслей верное, но немного не дожал)
    private ResponseEntity<ErrorResponse> handleException(StoreNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "The store with this name is not supported", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}