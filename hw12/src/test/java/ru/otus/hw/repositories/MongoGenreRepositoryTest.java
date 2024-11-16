package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DisplayName("Репозиторий на основе Mongo для работы с жанрами ")
@DirtiesContext(classMode = BEFORE_CLASS)
class MongoGenreRepositoryTest extends AbstractTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать жанры по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var collect = dbGenres.stream().map(Genre::getId).collect(Collectors.toUnmodifiableSet());
        var actualGenre = repository.findAllById(collect);
        assertThat(actualGenre).isNotEmpty()
                .containsExactlyElementsOf(dbGenres);
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