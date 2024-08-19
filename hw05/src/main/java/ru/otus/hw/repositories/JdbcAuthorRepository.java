package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query("SELECT id, full_name FROM authors",
                new EmptySqlParameterSource(),
                new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        List<Author> authors = jdbcTemplate.query(
                "SELECT id, full_name FROM authors WHERE id =:id",
                new HashMap<String, Object>() {{
                    put("id", id);
                }},
                new AuthorRowMapper()
        );

        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));

    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
