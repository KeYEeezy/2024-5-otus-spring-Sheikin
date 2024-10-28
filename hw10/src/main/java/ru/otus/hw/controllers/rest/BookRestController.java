package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping({"api/book"})
    public List<BookDto> getAllBooks() {
        return this.bookService.findAll();
    }

    @GetMapping({"api/book/{id}"})
    public BookDto getAllBooks(@PathVariable("id") String id) {
        return this.bookService.findById(id);
    }

}
