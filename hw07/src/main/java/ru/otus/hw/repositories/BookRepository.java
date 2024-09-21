package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(value = "Book.authorsAndGenres", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> findById(Long id);

    @Override
    @EntityGraph(value = "Book.authors", type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAll();
}
