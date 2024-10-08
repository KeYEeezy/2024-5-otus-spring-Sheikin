package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DisplayName("Сервис для работы с книгами ")
@Import({BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class,
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class,
        CommentMapperImpl.class
})
@Transactional(propagation = Propagation.NEVER)
class BookServiceTest extends AbstractTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private BookMapper bookMapper;


    @DisplayName("должен добавлять новую книгу")
    @Test
    void shouldAddNewBook() {
        var expectedBook = new BookDto(dbBooks.size() + 1L, "BookTitle_10",
                authorMapper.toDto(dbAuthors.get(0)),
                List.of(genreMapper.toDto(dbGenres.get(0)), genreMapper.toDto(dbGenres.get(1))));
        var actualBook = bookService.create("BookTitle_10", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() {
        var actualBook = bookService.update(1L, "BookTitle_10", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));
        var expectedBook = new BookDto(1L, "BookTitle_10",
                authorMapper.toDto(dbAuthors.get(0)),
                List.of(genreMapper.toDto(dbGenres.get(0)), genreMapper.toDto(dbGenres.get(1))));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        bookService.deleteById(3L);
        var actualBook = bookService.findById(3L);
        assertThat(actualBook).isEmpty();
    }

    @Nested
    @DirtiesContext(classMode = BEFORE_CLASS)
    class BookServiceFindTest {
        @DisplayName("должен найти книгу по id")
        @ParameterizedTest
        @MethodSource("ru.otus.hw.services.BookServiceTest#getDbBooks")
        void shouldFindBookById(Book expectedBook) {
            var actualBookDto = bookService.findById(expectedBook.getId());
            var dtoExpectedBook = bookMapper.toDto(expectedBook);
            assertThat(actualBookDto).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .ignoringCollectionOrder()
                    .isEqualTo(dtoExpectedBook);
        }

        @DisplayName("должен найти все книги")
        @Test
        void shouldFindAllBooks() {
            var actualBooks = bookService.findAll().stream().map(bookMapper::toEntity).toList();
            assertThat(actualBooks)
                    .isNotEmpty()
                    .hasSize(dbBooks.size())
                    .usingRecursiveComparison()
                    .isEqualTo(dbBooks);
        }
    }
}
