package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/")
    public String getAllBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/book";
    }

    @GetMapping("/comment/{id}")
    public String getComments(@PathVariable("id") String bookId, Model model) {
        model.addAttribute("comments", commentService.findAllByBookId(bookId));
        model.addAttribute("book", bookService.findById(bookId).orElseThrow());
        return "comment/comment";
    }

    @GetMapping("/create")
    public String addBook(Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("book", BookEditDto.builder().build());
        return "book/create";
    }

    @PostMapping("/create")
    public String addBook(@Valid @ModelAttribute(name = "book") BookEditDto bookEditDto) {
        bookService.create(bookEditDto);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") String bookId, Model model) {
        var book = bookService.findById(bookId).orElseThrow();
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", createEditDto(book));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("bookId", bookId);
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute(name = "book") BookEditDto bookEditDto) {
        bookService.update(bookEditDto);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") String bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }

    private BookEditDto createEditDto(BookDto bookDto) {
        return BookEditDto.builder()
                .id(bookDto.getId())
                .authorId(bookDto.getAuthor().getId())
                .title(bookDto.getTitle())
                .genreIds(bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))
                .build();
    }
}
