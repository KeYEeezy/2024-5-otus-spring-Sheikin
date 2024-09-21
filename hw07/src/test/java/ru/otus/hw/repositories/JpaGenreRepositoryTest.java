package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
class JpaGenreRepositoryTest extends AbstractTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать жанры по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var collect = dbGenres.stream().map(Genre::getId).collect(Collectors.toUnmodifiableSet());
        var actualGenre = repository.findAllById(collect);
        var expectedGenres = dbGenres;
        assertThat(actualGenre).isNotEmpty()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repository.findAll();
        var expectedGenres = dbGenres;
        assertThat(actualGenres).isNotEmpty()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedGenres);;
    }
}