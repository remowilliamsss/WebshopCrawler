package ru.egorov.StoreCrawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.egorov.StoreCrawler.parser.product.ProductParser;

import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class CrawlerTest {
    @Autowired
    private Map<String, ProductParser> productParsers;

    public static final String FOOTBOX_SQL = "/crawler/delete-footbox-products.sql";
    public static final String SNEAKERHEAD_SQL = "/crawler/delete-sneakerhead-products.sql";

    @Test
    @Sql(value = {SNEAKERHEAD_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlSneakerhead() {
        productParsers.get("sneakerheadProductParser")
                .parseProducts();
    }

    @Test
    @Sql(value = {FOOTBOX_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void crawlFootbox() {
        productParsers.get("footboxProductParser")
                .parseProducts();
    }
}
