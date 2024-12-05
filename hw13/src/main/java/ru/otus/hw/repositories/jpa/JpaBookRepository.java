package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.jpa.JpaBook;

import java.util.List;

@Repository
public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    List<JpaBook> findAll();
}
