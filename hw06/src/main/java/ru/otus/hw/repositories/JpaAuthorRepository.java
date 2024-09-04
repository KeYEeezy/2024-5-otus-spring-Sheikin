package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class JpaAuthorRepository implements AuthorRepository {

    private final EntityManager entityManager;

    @Autowired
    public JpaAuthorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
       return Optional.ofNullable(entityManager.find(Author.class, id));
    }
}
