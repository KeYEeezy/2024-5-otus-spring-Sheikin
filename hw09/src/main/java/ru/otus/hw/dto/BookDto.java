package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    private Set<String> genreIds;

    private String authorId;

    public BookDto(String id, String title, AuthorDto author, List<GenreDto> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    public BookDto(String id, String title, Set<String> genreIds, String authorId) {
        this.id = id;
        this.title = title;
        this.genreIds = genreIds;
        this.authorId = authorId;
    }
}
