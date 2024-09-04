package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto insert(String text, long bookId) {
        return commentMapper.toDto(save(0, text, bookId));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text, long bookId) {
        return commentMapper.toDto(save(id, text, bookId));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
