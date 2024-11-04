package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = BEFORE_CLASS)
class MongoCommentRepositoryTest extends AbstractTest {

    @Autowired
    private CommentRepository repository;



    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    @Order(1)
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по идентификатору книги")
    @Test
    @Order(2)
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = repository.findAllByBookId("1");
        var expectedComments = dbComments
                .stream()
                .filter(val -> val.getBook().getId().equals("1"))
                .toList();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @Order(3)
    void shouldSaveNewComment() {
        var book = dbComments.get(0).getBook();
        var expectedComment = new Comment("10", "New Comment", book);
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @Order(4)
    void shouldSaveUpdateComment() {
        var expectedComment = dbComments.get(0);
        expectedComment.setText("New Comment");

        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    @Order(5)
    void shouldDeleteComment() {
        assertThat(repository.findById("1")).isPresent();
        repository.deleteById("1");
        assertThat(repository.findById("1")).isEmpty();
    }
}
