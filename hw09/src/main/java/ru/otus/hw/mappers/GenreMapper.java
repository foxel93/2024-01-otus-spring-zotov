package ru.otus.hw.mappers;

import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

public class GenreMapper {
    public static GenreDto toGenreDto(Genre genre) {
        return new GenreDto(
            genre.getId(),
            genre.getName()
        );
    }
}
