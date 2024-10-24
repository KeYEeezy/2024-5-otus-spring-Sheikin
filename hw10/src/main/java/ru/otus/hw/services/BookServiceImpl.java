package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
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
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto create(BookCreateDto editDto) {
        var authorId = editDto.getAuthorId();
        var genreIds = editDto.getGenreIds();
        var title = editDto.getTitle();

        var author = authorRepository.findById(editDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genreIds);

        if (CollectionUtils.isEmpty(genres) || genreIds.size() != genres.size()) {
            throw new NotFoundException("One or all genres with ids %s not found".formatted(genreIds));
        }

        var book = new Book(null, title, author, genres);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto editDto) {
        var authorId = editDto.getAuthorId();
        var genreIds = editDto.getGenreIds();
        var title = editDto.getTitle();
        var book = bookRepository.findById(editDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %s not found".formatted(editDto.getId())));

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genreIds);

        if (CollectionUtils.isEmpty(genres) || genreIds.size() != genres.size()) {
            throw new NotFoundException("One or all genres with ids %s not found".formatted(genreIds));
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
