package ru.otus.hw.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}
