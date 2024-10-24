package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String commentId);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto create(String text, String bookId);

    CommentDto update(String commentId, String text);

    void deleteById(String commentId);
}
