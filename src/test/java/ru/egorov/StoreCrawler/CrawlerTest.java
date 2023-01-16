package ru.egorov.StoreCrawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.egorov.StoreCrawler.parser.StoreParser;
import ru.egorov.StoreCrawler.service.DispatcherService;

import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class CrawlerTest {
    @Autowired
    private Map<String, StoreParser> storeParsers;
    @Autowired
    private DispatcherService dispatcherService;

    @Test
    @Sql(value = {"/crawler/delete-sneakerhead-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlSneakerhead() {
        var parser = storeParsers.get("sneakerheadStoreParser");

        dispatcherService.getProductsService(parser.getStore())
                .updateProducts(parser.parseProducts());
    }

    @Test
    @Sql(value = {"/crawler/delete-footbox-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlFootbox() {
        var parser = storeParsers.get("footboxStoreParser");

        dispatcherService.getProductsService(parser.getStore())
                .updateProducts(parser.parseProducts());
    }
}
