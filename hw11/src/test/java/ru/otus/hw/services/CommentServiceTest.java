package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.AbstractTest;
import ru.otus.hw.mappers.CommentMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DisplayName("Сервис для работы с комментариями ")
@Import({CommentServiceImpl.class,
        CommentMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceTest extends AbstractTest {

    @Autowired
    private CommentService commentService;

    @DisplayName("должен добавлять новый комментарий")
    @Test
    void shouldAddNewComment() {
        var actualComment = commentService.create("Some comment", "1");
        var expectedComment = commentService.findById(actualComment.getId());
        assertAll(
                () -> assertThat(actualComment).isNotNull(),
                () -> assertThat(expectedComment).isPresent(),
                () -> assertThat(actualComment.getId()).isEqualTo(expectedComment.get().getId()),
                () -> assertThat(actualComment.getText()).isEqualTo(expectedComment.get().getText())
        );
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = commentService.findById("1");
        var actualComment = commentService.update(comment.get().getId(), "New comment");
        var expectedComment = commentService.findById("1");
        assertAll(
                () -> assertThat(actualComment).isNotNull(),
                () -> assertThat(expectedComment).isPresent(),
                () -> assertThat(actualComment.getId()).isEqualTo(expectedComment.get().getId()),
                () -> assertThat(actualComment.getText()).isEqualTo(expectedComment.get().getText())
        );
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteBookById() {
        commentService.deleteById("2");
        var actualComment = commentService.findById("2");
        assertThat(actualComment).isEmpty();
    }

    @Nested
    @DirtiesContext(classMode = BEFORE_CLASS)
    class CommentServiceFindTest {
        @DisplayName("должен найти комментарий по id")
        @Test
        void shouldFindCommentById() {
            var actualComment = commentService.findById("1");
            assertAll(
                    () -> assertThat(actualComment).isPresent(),
                    () -> assertThat(actualComment.get().getId()).isEqualTo("1")
            );
        }

        @DisplayName("должен найти комментарии по id книги")
        @Test
        void shouldFindCommentByBookId() {
            var actualComments = commentService.findAllByBookId("1");
            assertThat(actualComments)
                    .isNotEmpty()
                    .hasSize(2)
                    .allMatch(b -> b.getId() != null);
        }

    }
}