package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotNull
    private Long id;

    @NotEmpty
    private String title;

    private AuthorDto author;

    @NotEmpty
    private List<GenreDto> genres;
}
