package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto create(String title, long authorId, Set<Long> genreId);

    BookDto update(long id, String title, long authorId, Set<Long> genreId);

    void deleteById(long id);
}
