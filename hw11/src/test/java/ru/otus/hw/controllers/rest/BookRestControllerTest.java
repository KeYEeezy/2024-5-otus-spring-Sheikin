package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.controllers.AbstractControllerTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookRestControllerTest extends AbstractControllerTest {

    @Configuration
    @ComponentScan
    public static class TestMongoConfig {
        @Bean
        public MongoTemplate mongoTemplate() {
            return mock(MongoTemplate.class);
        }
    }


    @Test
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getAllBooksRest() throws Exception {

        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getAllBookByIdRest() throws Exception {

        mockMvc.perform(get("/api/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBooksRestUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/api/book"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }

    @Test
    void getBookByIdRestUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/api/book/1"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}
