package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.otus.hw.repositories.sql.SQLQueries.DELETE_BOOK_BY_ID;
import static ru.otus.hw.repositories.sql.SQLQueries.FIND_ALL_BOOKS;
import static ru.otus.hw.repositories.sql.SQLQueries.FIND_BOOK_BY_ID;
import static ru.otus.hw.repositories.sql.SQLQueries.INSERT_BOOK;
import static ru.otus.hw.repositories.sql.SQLQueries.UPDATE_BOOK;


@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BOOK_BY_ID,
                    new HashMap<String, Object>() {{
                        put("id", id);
                    }}, new BookRowMapper()

            ));
        } catch (DataAccessException e) {
            log.error("Error when searching for a book by ID");
            return Optional.empty();

        }

    }

    @Override
    public List<Book> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_BOOKS, new EmptySqlParameterSource(), new BookRowMapper());
        } catch (DataAccessException e) {
            log.error("Error when searching for all books");
            return Collections.emptyList();
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        try {
            jdbcTemplate.update(
                    DELETE_BOOK_BY_ID,
                    new HashMap<String, Object>() {{
                        put("id", id);
                    }}
            );
        } catch (DataAccessException e) {
            log.error("Error when deleting for a book by ID");
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        var parameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("authorId", book.getAuthor().getId(), Types.BIGINT)
                .addValue("genreId", book.getGenre().getId(), Types.BIGINT);
        jdbcTemplate.update(INSERT_BOOK,
                parameterSource,
                keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = Map.of("id", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId());
        var updatedBookId = jdbcTemplate.update(UPDATE_BOOK,
                params);
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (updatedBookId == 0) {
            throw new EntityNotFoundException("Error when updating for a book");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long booksId = rs.getLong("books.id");
            String booksTitle = rs.getString("books.title");
            long authorsId = rs.getLong("authors.id");
            String authorsFullName = rs.getString("authors.full_name");
            long genresId = rs.getLong("genres.id");
            String genresName = rs.getString("genres.name");
            return new Book(booksId, booksTitle,
                    new Author(authorsId, authorsFullName),
                    new Genre(genresId, genresName));
        }
    }
}
