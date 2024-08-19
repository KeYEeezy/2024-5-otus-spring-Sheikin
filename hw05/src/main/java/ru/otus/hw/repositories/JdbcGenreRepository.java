package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;



@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM genres", new EmptySqlParameterSource(), new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        List<Genre> genres = jdbcTemplate.query(
                "SELECT id, name FROM genres WHERE id =:id",
                new HashMap<String, Object>() {{
                    put("id", id);
                }}, new GenreRowMapper()

        );
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
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
