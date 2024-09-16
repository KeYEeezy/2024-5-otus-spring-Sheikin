package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;


@Repository
@Slf4j
public class JpaGenreRepository implements GenreRepository {

    private final EntityManager entityManager;

    @Autowired
    public JpaGenreRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public List<Genre> findByIds(Set<Long> ids) {
      return entityManager.createQuery("select g from Genre g where g.id in (:ids)", Genre.class)
              .setParameter("ids", ids)
              .getResultList();
    }
}
