package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long commentId);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String text, long bookId);

    CommentDto update(long commentId, String text);

    void deleteById(long commentId);
}
