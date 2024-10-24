package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {


    @ChangeSet(order = "001", id = "drop", author = "nsheykin", runAlways = true)
    public void drop(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "init-library", author = "nsheykin")
    public void init(AuthorRepository authorRepository, GenreRepository genreRepository,
                     BookRepository bookRepository, CommentRepository commentRepository) {
        Author author1 = authorRepository.save(new Author("1", "Fyodor Dostoevsky"));
        Author author2 = authorRepository.save(new Author("2", "Ursula Kroeber Le Guin"));
        Author author3 = authorRepository.save(new Author("3", "Sergei Lukyanenko"));

        Genre genre1 = genreRepository.save(new Genre("1", "Novel"));
        Genre genre2 = genreRepository.save(new Genre("2", "Fantasy"));
        Genre genre3 = genreRepository.save(new Genre("3", "SciFi"));

        Book book1 = bookRepository.save(new Book("1", "Crime and Punishment", author1, List.of(genre1)));
        Book book2 = bookRepository.save(new Book("2", "A Wizard of Earthsea", author2, List.of(genre2)));
        Book book3 = bookRepository.save(new Book("3", "Labyrinth of Reflections", author3, List.of(genre2, genre3)));

        commentRepository.save(new Comment("1", "Comment_1", book1));
        commentRepository.save(new Comment("2", "Comment_2", book1));
        commentRepository.save(new Comment("3", "Comment_3", book2));
        commentRepository.save(new Comment("4", "Comment_4", book3));
    }
}
