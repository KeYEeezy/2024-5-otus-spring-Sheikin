package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class GenreControllerTest extends AbstractControllerTest {

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
    void getAllGenres() throws Exception {
        when(genreService.findAll()).thenReturn(getDbGenres());

        mockMvc.perform(get("/genre"))
                .andExpect(view().name("genre/genre"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllGenresUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/genre"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}