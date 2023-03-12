package ru.egorov.StoreCrawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.egorov.StoreCrawler.model.product.StoreType;
import ru.egorov.StoreCrawler.service.DispatcherService;
import ru.egorov.StoreCrawler.service.parser.product.ProductParserService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class CrawlerTest {
    public static final String FOOTBOX_SQL = "/crawler/delete-footbox-products.sql";
    public static final String SNEAKERHEAD_SQL = "/crawler/delete-sneakerhead-products.sql";

    @Autowired
    private Map<String, ProductParserService> productParsers;
    @Autowired
    private DispatcherService dispatcherService;

    @Test
    @Sql(value = {SNEAKERHEAD_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlSneakerhead() {
        productParsers.get("sneakerheadProductParserService")
                .parseAll();

        var products = dispatcherService.getProductsService(StoreType.sneakerhead)
                .findAll();

        assertTrue(products.size() > 900);
    }

    @Test
    @Sql(value = {FOOTBOX_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlFootbox() {
        productParsers.get("footboxProductParserService")
                .parseAll();

        var products = dispatcherService.getProductsService(StoreType.footbox)
                .findAll();

        assertTrue(products.size() > 1000);
    }
}
