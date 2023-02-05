package ru.egorov.StoreCrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.egorov.StoreCrawler.controller.ProductController;
import ru.egorov.StoreCrawler.controller.handler.ControllerExceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/search/create-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/search/delete-products.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProviderTest {
    public static final String FIND_BY_STORE_URL = "/api/products";
    public static final String SNEAKERHEAD = "sneakerhead";
    public static final String FOOTBOX = "footbox";
    public static final String MESSAGE = "message";
    public static final String SOME_STRING = "some string";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void gainSneakerheadProducts() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SNEAKERHEAD))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[4]")
                        .exists())
                .andExpect(jsonPath("products[5]")
                        .doesNotExist());
    }

    @Test
    public void gainSneakerheadProductsWithPagination() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SNEAKERHEAD)
                        .param(ProductController.PAGE, "0")
                        .param(ProductController.SIZE, "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[1]")
                        .exists())
                .andExpect(jsonPath("products[2]")
                        .doesNotExist());
    }

    @Test
    public void gainFootboxProducts() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, FOOTBOX))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[4]")
                        .exists())
                .andExpect(jsonPath("products[5]")
                        .doesNotExist());
    }

    @Test
    public void gainFootboxProductsWithPagination() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, FOOTBOX)
                        .param(ProductController.PAGE, "1")
                        .param(ProductController.SIZE, "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[1]")
                        .exists())
                .andExpect(jsonPath("products[2]")
                        .doesNotExist());
    }

    @Test
    public void gainProductsFromNotSupportedStore() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SOME_STRING))
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(ControllerExceptionHandler.STORE_NOT_SUPPORTED));
    }

    @Test
    public void gainProductsFromNotSupportedStoreWithPagination() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SOME_STRING)
                        .param(ProductController.PAGE, "0")
                        .param(ProductController.SIZE, "2"))
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(ControllerExceptionHandler.STORE_NOT_SUPPORTED));
    }
}
