package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest extends AbstractTest {

    @Autowired
    private JpaGenreRepository repository;

    @DisplayName("должен загружать жанры по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var collect = dbGenres.stream().map(Genre::getId).collect(Collectors.toUnmodifiableSet());
        var actualGenre = repository.findByIds(collect);
        assertThat(actualGenre).isNotEmpty()
                .containsExactlyElementsOf(dbGenres);
    }

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }
}