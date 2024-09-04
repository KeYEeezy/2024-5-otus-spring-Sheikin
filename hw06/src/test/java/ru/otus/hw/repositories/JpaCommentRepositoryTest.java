package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.models.Comment;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest extends AbstractTest {

    @Autowired
    private JpaCommentRepository repository;



    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по идентификатору книги")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = repository.findAllByBookId(1L);
        var expectedComments = dbComments
                .stream()
                .filter(val -> val.getBook().getId() == 1)
                .toList();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = dbComments.get(0).getBook();
        var expectedComment = new Comment(10L, "New Comment", book);
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdateComment() {
        var expectedComment = dbComments.get(0);
        expectedComment.setText("New Comment");

        assertThat(repository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        assertThat(repository.findById(1L)).isPresent();
        repository.deleteById(1L);
        assertThat(repository.findById(1L)).isEmpty();
    }
}
