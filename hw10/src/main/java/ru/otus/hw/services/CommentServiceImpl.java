package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              BookRepository bookRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(commentMapper::toDto);
    }

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto create(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(null, text, book);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(String commentId, String text) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id %s not found".formatted(commentId)));
        comment.setText(text);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
