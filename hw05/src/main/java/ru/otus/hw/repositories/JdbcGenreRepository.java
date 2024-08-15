package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ru.otus.hw.repositories.sql.SQLQueries.FIND_ALL_GENRES;
import static ru.otus.hw.repositories.sql.SQLQueries.FIND_GENRE_BY_ID;


@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_GENRES, new EmptySqlParameterSource(), new GenreRowMapper());
        } catch (DataAccessException e) {
            log.error("Error when searching for all genres");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_GENRE_BY_ID,
                    new HashMap<String, Object>() {{
                        put("id", id);
                    }}, new GenreRowMapper()

            ));
        } catch (DataAccessException e) {
            log.error("Error when searching for an genre by ID");
            return Optional.empty();

        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
