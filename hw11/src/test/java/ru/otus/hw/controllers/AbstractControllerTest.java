package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.rest.AuthorRestController;
import ru.otus.hw.controllers.rest.BookRestController;
import ru.otus.hw.controllers.rest.CommentRestController;
import ru.otus.hw.controllers.rest.GenreRestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;


@WebMvcTest(controllers = {
        AuthorController.class,
        BookController.class,
        GenreController.class,
        CommentRestController.class,
        AuthorRestController.class,
        GenreRestController.class,
        BookRestController.class
})
@Import({SecurityConfig.class})
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @MockBean
    protected BookService bookService;
    @MockBean
    protected AuthorService authorService;
    @MockBean
    protected GenreService genreService;
    @MockBean
    protected CommentService commentService;

    @Autowired
    protected MockMvc mockMvc;


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
                new CommentDto("1", "Comment_1"),
                new CommentDto("2", "Comment_2"),
                new CommentDto("3", "Comment_3"),
                new CommentDto("4", "Comment_4")
        );
    }
    private static List<CommentDto> getDbComments() {
        var books = getDbBooks(getDbAuthors(), getDbGenres());
        return getDbComments(books);
    }
}
