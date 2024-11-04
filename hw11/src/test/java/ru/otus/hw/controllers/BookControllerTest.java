package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class BookControllerTest extends AbstractControllerTest {

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
    void getAllBook() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
                .andExpect(view().name("book/book"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void createBook() throws Exception {
        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);

        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void editBook() throws Exception {
        var book = books.stream().filter(val -> val.getId().equals("2")).findFirst().get();

        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);
        given(bookService.findById(anyString())).willReturn(book);

        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);
        given(bookService.findById(anyString())).willReturn(book);

        mockMvc.perform(get("/edit/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attributeExists("bookId"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void updateBook() throws Exception {
        var author = dbAuthors.stream().filter(val -> val.getId().equals("2")).findFirst().get();
        var genre = dbGenres.stream().filter(val -> val.getId().equals("2")).findFirst().get();
        var request = new BookUpdateDto("10", "BookTitle_10", Collections.singleton(genre.getId()), author.getId());
        var bookDto = new BookDto(request.getId(), request.getTitle(), author, List.of(genre));

        when(bookService.update(any(BookUpdateDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/edit").flashAttr("book", request))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void saveBookInsert() throws Exception {
        var author = dbAuthors.stream().filter(val -> val.getId().equals("2")).findFirst().get();
        var genre = dbGenres.stream().filter(val -> val.getId().equals("1")).findFirst().get();
        var request = new BookCreateDto( "BookTitle_10", Collections.singleton(genre.getId()), author.getId());
        var bookDto = new BookDto("10", request.getTitle(), author, List.of(genre));

        var newBooks = new ArrayList<>(books);
        newBooks.add(bookDto);

        when(bookService.create(any(BookCreateDto.class))).thenReturn(bookDto);
        given(bookService.findAll()).willReturn(newBooks);

        mockMvc.perform(post("/create").flashAttr("book", request))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById("1");

        mockMvc.perform(post("/delete/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }
}