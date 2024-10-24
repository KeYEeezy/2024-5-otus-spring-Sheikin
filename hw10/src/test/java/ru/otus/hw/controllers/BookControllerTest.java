package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;

    @Configuration
    @ComponentScan
    static class TestConfig {
        @Bean
        public MongoTemplate mongoTemplate() {
            return mock(MongoTemplate.class);
        }
    }

    public static List<AuthorDto> dbAuthors;

    public static List<GenreDto> dbGenres;

    public static List<BookDto> books;

    public static List<CommentDto> dbComments;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        books = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments(books);
    }


    public static List<AuthorDto> getDbAuthors() {
        return List.of(
                new AuthorDto("1", "Fyodor Dostoevsky"),
                new AuthorDto("2", "Ursula Kroeber Le Guin"),
                new AuthorDto("3", "Sergei Lukyanenko")
        );
    }

    public static List<GenreDto> getDbGenres() {
        return List.of(
                new GenreDto("1", "Novel"),
                new GenreDto("2", "Fantasy"),
                new GenreDto("3", "SciFi")
        );
    }

    public static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return List.of(
                new BookDto("1", "Crime and Punishment", dbAuthors.get(0), List.of(dbGenres.get(0))),
                new BookDto("2", "A Wizard of Earthsea", dbAuthors.get(1), List.of(dbGenres.get(1))),
                new BookDto("3", "Labyrinth of Reflections", dbAuthors.get(2), List.of(dbGenres.get(1), dbGenres.get(2)))
        );
    }

    public static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<CommentDto> getDbComments(List<BookDto> books) {
        return List.of(
                new CommentDto("1", "Comment_1", books.get(0)),
                new CommentDto("2", "Comment_2", books.get(0)),
                new CommentDto("3", "Comment_3", books.get(1)),
                new CommentDto("4", "Comment_4", books.get(2))
        );
    }
    private static List<CommentDto> getDbComments() {
        var books = getDbBooks(getDbAuthors(), getDbGenres());
        return getDbComments(books);
    }

    @Test
    void getAllBook() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
                .andExpect(view().name("book/book"))
                .andExpect(status().isOk());
    }

    @Test
    void createBook() throws Exception {
        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);

        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void editBook() throws Exception {
        var book = books.stream().filter(val -> val.getId().equals("2")).findFirst();

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
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById("1");

        mockMvc.perform(post("/delete/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    private BookUpdateDto createEditDto(BookDto bookDto) {
        return BookUpdateDto.builder()
                .id(bookDto.getId())
                .authorId(bookDto.getAuthor().getId())
                .title(bookDto.getTitle())
                .genreIds(bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))
                .build();
    }
}