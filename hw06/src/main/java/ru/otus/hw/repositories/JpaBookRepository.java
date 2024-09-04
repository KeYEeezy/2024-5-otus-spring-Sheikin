package ru.otus.hw.repositories;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;


@Repository
@Slf4j
public class JpaBookRepository implements BookRepository {

    private EntityGraph<?> bookEntityGraph;

    private final EntityManager entityManager;

    @Autowired
    public JpaBookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        bookEntityGraph = entityManager.getEntityGraph("book-graph");
    }

    @Override
    public Optional<Book> findById(long id) {
            return Optional.ofNullable(entityManager.find(Book.class, id, Map.of(FETCH.getKey(), bookEntityGraph)));
    }

    @Override
    public List<Book> findAll() {
        var query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), bookEntityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
        } else {
            book = entityManager.merge(book);
        }
        return book;
    }

    @Override
    public void deleteById(long id) {
        var book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
