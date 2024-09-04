package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaCommentRepository implements CommentRepository {

    private final EntityManager entityManager;

    @Autowired
    public JpaCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Comment> findById(long id) {
            return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
            var query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.book.id = :book_id", Comment.class);
            query.setParameter("book_id", bookId);
            return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
        } else {
            comment = entityManager.merge(comment);
        }
        return comment;
    }

    @Override
    public void deleteById(long id) {
        var comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }
}
