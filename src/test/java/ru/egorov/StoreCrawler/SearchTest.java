package ru.egorov.StoreCrawler;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.egorov.StoreCrawler.dto.search.SearchRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/search/create-products-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/search/create-products-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SearchTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    public static final String SEARCH_URL = "/api/products/search";
    public static final String FIND_BY_SKU_URL = "/api/products/find_by_sku";
    public static final String FIND_BY_STORE_URL = "/api/products";
    public static final String SNEAKERHEAD = "sneakerhead";
    public static final String FOOTBOX = "footbox";
    public static final String QUERY_FOR_SNEAKERHEAD = "Nike Joyride ENV ISPA";
    public static final String QUERY_FOR_FOOTBOX = "Jordan Legacy 312 \"Exploration Unit\"";
    public static final String QUERY_FOR_ALL_STORES = "Air Trainer 1";
    public static final String QUERY_FOR_MANY_RESULTS = "nike";
    public static final String SKU_FOR_SNEAKERHEAD = "BV4584-400";
    public static final String SKU_FOR_FOOTBOX = "FB1875-141";
    public static final String SKU_FOR_ALL_STORES = "DR7515-200";
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String MESSAGE = "message";
    public static final String SOME_STRING = "some string";
    public static final String EMPTY_QUERY_MESSAGE = "query - must not be empty;";
    public static final String FIRST_RESULT_SECOND_STORE = "$[0].differences[1]";
    public static final String FIRST_RESULT_FIRST_STORE_NAME = "$[0].differences[0].storeType";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void searchForProductAvailableInAllStores() throws Exception {
        SearchRequest searchRequest = new SearchRequest(QUERY_FOR_ALL_STORES);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .exists());
    }

    @Test
    public void searchForProductOnlyAvailableOnSneakerhead() throws Exception {
        SearchRequest searchRequest = new SearchRequest(QUERY_FOR_SNEAKERHEAD);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_FIRST_STORE_NAME)
                        .value(SNEAKERHEAD))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .doesNotExist());
    }

    @Test
    public void searchForProductOnlyAvailableOnFootbox() throws Exception {
        SearchRequest searchRequest = new SearchRequest(QUERY_FOR_FOOTBOX);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_FIRST_STORE_NAME)
                        .value(FOOTBOX))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .doesNotExist());
    }

    @Test
    public void searchForNotAvailableProduct() throws Exception {
        SearchRequest searchRequest = new SearchRequest(SOME_STRING);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$")
                        .isEmpty());
    }

    @Test
    public void searchWithManyResults() throws Exception {
        SearchRequest searchRequest = new SearchRequest(QUERY_FOR_MANY_RESULTS);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[3]")
                        .exists());
    }

    @Test
    public void searchForNullQuery() throws Exception {
        SearchRequest searchRequest = new SearchRequest(null);

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(EMPTY_QUERY_MESSAGE));
    }

    @Test
    public void searchForEmptyQuery() throws Exception {
        SearchRequest searchRequest = new SearchRequest("");

        this.mockMvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(EMPTY_QUERY_MESSAGE));
    }

    @Test
    public void findBySkuForProductAvailableInAllStores() throws Exception {
        SearchRequest searchRequest = new SearchRequest(SKU_FOR_ALL_STORES);

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .exists());
    }

    @Test
    public void findBySkuForProductOnlyAvailableOnSneakerhead() throws Exception {
        SearchRequest searchRequest = new SearchRequest(SKU_FOR_SNEAKERHEAD);

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_FIRST_STORE_NAME)
                        .value(SNEAKERHEAD))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .doesNotExist());
    }

    @Test
    public void findBySkuForProductOnlyAvailableOnFootbox() throws Exception {
        SearchRequest searchRequest = new SearchRequest(SKU_FOR_FOOTBOX);

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(FIRST_RESULT_FIRST_STORE_NAME)
                        .value(FOOTBOX))
                .andExpect(jsonPath(FIRST_RESULT_SECOND_STORE)
                        .doesNotExist());
    }

    @Test
    public void findBySkuForNotAvailableProduct() throws Exception {
        SearchRequest searchRequest = new SearchRequest(SOME_STRING);

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$")
                        .isEmpty());
    }

    @Test
    public void findBySkuForNullQuery() throws Exception {
        SearchRequest searchRequest = new SearchRequest(null);

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(EMPTY_QUERY_MESSAGE));
    }

    @Test
    public void findBySkuForEmptyQuery() throws Exception {
        SearchRequest searchRequest = new SearchRequest("");

        this.mockMvc.perform(post(FIND_BY_SKU_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchRequest)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(EMPTY_QUERY_MESSAGE));
    }

    @Test
    public void findByStoreForSneakerhead() throws Exception {
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
    public void findByStoreWithPaginationForSneakerhead() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SNEAKERHEAD)
                        .param(PAGE, "0")
                        .param(SIZE, "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[1]")
                        .exists())
                .andExpect(jsonPath("products[2]")
                        .doesNotExist());
    }

    @Test
    public void findByStoreForFootbox() throws Exception {
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
    public void findByStoreWithPaginationForFootbox() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, FOOTBOX)
                        .param(PAGE, "1")
                        .param(SIZE, "2"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("products[1]")
                        .exists())
                .andExpect(jsonPath("products[2]")
                        .doesNotExist());
    }

    @Test
    public void findByStoreForNotSupportedStore() throws Exception {
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
    public void findByStoreWithPaginationForNotSupportedStore() throws Exception {
        this.mockMvc.perform(get(FIND_BY_STORE_URL)
                        .param(ProductController.STORE, SOME_STRING)
                        .param(PAGE, "0")
                        .param(SIZE, "2"))
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MESSAGE)
                        .value(ControllerExceptionHandler.STORE_NOT_SUPPORTED));
    }
}
