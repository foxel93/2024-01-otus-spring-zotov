package ru.otus.hw.mappers;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {
    public GenreDto toGenreDto(Genre genre) {
        return new GenreDto(
            genre.getId(),
            genre.getName()
        );
    }

    public Genre toGenre(GenreDto genreDto) {
        return new Genre(
            genreDto.getId(),
            genreDto.getName()
        );
    }

    public List<Genre> toGenres(List<GenreDto> genreDtoList) {
        return genreDtoList
            .stream()
            .map(this::toGenre)
            .toList();
    }
}
