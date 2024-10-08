package ru.otus.hw;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@DataMongoTest
public abstract class AbstractTest {

    public static List<Author> dbAuthors;

    public static List<Genre> dbGenres;

    public static List<Book> dbBooks;

    public static List<Comment> dbComments;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments(dbBooks);
    }


    public static List<Author> getDbAuthors() {
        return List.of(
                new Author("1", "Fyodor Dostoevsky"),
                new Author("2", "Ursula Kroeber Le Guin"),
                new Author("3", "Sergei Lukyanenko")
        );
    }

    public static List<Genre> getDbGenres() {
        return List.of(
                new Genre("1", "Novel"),
                new Genre("2", "Fantasy"),
                new Genre("3", "SciFi")
        );
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return List.of(
                new Book("1", "Crime and Punishment", dbAuthors.get(0), List.of(dbGenres.get(0))),
                new Book("2", "A Wizard of Earthsea", dbAuthors.get(1), List.of(dbGenres.get(1))),
                new Book("3", "Labyrinth of Reflections", dbAuthors.get(2), List.of(dbGenres.get(1), dbGenres.get(2)))
        );
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Comment> getDbComments(List<Book> books) {
        return List.of(
                new Comment("1", "Comment_1", books.get(0)),
                new Comment("2", "Comment_2", books.get(0)),
                new Comment("3", "Comment_3", books.get(1)),
                new Comment("4", "Comment_4", books.get(2))
        );
    }
    private static List<Comment> getDbComments() {
        var books = getDbBooks(getDbAuthors(), getDbGenres());
        return getDbComments(books);
    }
}
