package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

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
        model.addAttribute("book", BookDto.builder().build());
        return "book/create";
    }

    @PostMapping("/create")
    public String addBook(@ModelAttribute BookDto bookDto) {
        bookService.create(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreIds());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") String bookId, Model model) {
        var book = bookService.findById(bookId).orElseThrow();
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "book/edit";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") String bookId, @ModelAttribute BookDto book) {
        book.setId(bookId);
        bookService.update(bookId, book.getTitle(), book.getAuthorId(), book.getGenreIds());
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") String bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }
}