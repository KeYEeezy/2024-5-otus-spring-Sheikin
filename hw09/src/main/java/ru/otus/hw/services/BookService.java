package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(String id);

    List<BookDto> findAll();

    BookDto create(BookEditDto bookEditDto);

    BookDto update(BookEditDto bookEditDto);

    void deleteById(String bookId);
}
