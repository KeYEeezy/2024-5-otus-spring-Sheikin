package ru.otus.hw.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@NoArgsConstructor(force = true)
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(AuthorRepository authorRepository,
                           GenreRepository genreRepository,
                           BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, Set<Long> genreIds) {
        return bookMapper.toDto(save(0, title, authorId, genreIds));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genreIds) {
        return bookMapper.toDto(save(id, title, authorId, genreIds));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genreIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("AuthorDto with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIds(genreIds);
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
