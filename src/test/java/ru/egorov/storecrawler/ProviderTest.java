package ru.egorov.storecrawler;

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
import ru.egorov.storecrawler.controller.handler.ControllerExceptionHandler;
import ru.egorov.storecrawler.model.StoreType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/delete-products.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProviderTest {
    public static final String URL = "/api/products";

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
        this.mockMvc.perform(get(URL)
                        .param("store", StoreType.sneakerhead.toString()))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("content[4]")
                        .exists())
                .andExpect(jsonPath("content[5]")
                        .doesNotExist());
    }

    @Test
    public void gainSneakerheadProductsWithPagination() throws Exception {
        this.mockMvc.perform(get(URL)
                        .param("store", StoreType.sneakerhead.toString())
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("content[1]")
                        .exists())
                .andExpect(jsonPath("content[2]")
                        .doesNotExist());
    }

    @Test
    public void gainFootboxProducts() throws Exception {
        this.mockMvc.perform(get(URL)
                        .param("store", StoreType.footbox.toString()))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("content[4]")
                        .exists())
                .andExpect(jsonPath("content[5]")
                        .doesNotExist());
    }

    @Test
    public void gainFootboxProductsWithPagination() throws Exception {
        this.mockMvc.perform(get(URL)
                        .param("store", StoreType.footbox.toString())
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("content[1]")
                        .exists())
                .andExpect(jsonPath("content[2]")
                        .doesNotExist());
    }

    @Test
    public void gainProductsFromNotSupportedStore() throws Exception {
        this.mockMvc.perform(get(URL)
                        .param("store", "some string"))
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message")
                        .value(ControllerExceptionHandler.STORE_NOT_SUPPORTED));
    }

    @Test
    public void gainProductsFromNotSupportedStoreWithPagination() throws Exception {
        this.mockMvc.perform(get(URL)
                        .param("store", "some string")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message")
                        .value(ControllerExceptionHandler.STORE_NOT_SUPPORTED));
    }
}
