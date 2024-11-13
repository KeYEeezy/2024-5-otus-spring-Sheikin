package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AuthorControllerTest extends AbstractControllerTest {

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
    void getAllAuthor() throws Exception {
        mockMvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(view().name("author/author"));
    }

    @Test
    void getAllAuthorUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/author"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}