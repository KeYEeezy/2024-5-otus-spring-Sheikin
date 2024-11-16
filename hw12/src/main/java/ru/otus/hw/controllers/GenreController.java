package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GenreController {

    @GetMapping("/genre")
    public String getGenres() {
        return "genre/genre";
    }
}
