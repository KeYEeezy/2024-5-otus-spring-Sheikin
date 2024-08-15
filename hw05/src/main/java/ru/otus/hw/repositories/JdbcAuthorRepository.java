package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ru.otus.hw.repositories.sql.SQLQueries.FIND_ALL_AUTHORS;
import static ru.otus.hw.repositories.sql.SQLQueries.FIND_AUTHOR_BY_ID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_AUTHORS, new EmptySqlParameterSource(), new AuthorRowMapper());
        } catch (DataAccessException e) {
            log.error("Error when searching for all authors");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_AUTHOR_BY_ID,
                    new HashMap<String, Object>() {{
                        put("id", id);
                    }}, new AuthorRowMapper()

            ));
        } catch (DataAccessException e) {
            log.error("Error when searching for a author by ID");
            return Optional.empty();

        }
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
