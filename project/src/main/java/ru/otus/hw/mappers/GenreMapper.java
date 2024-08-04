package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;

@Component
@AllArgsConstructor
public class GenreMapper {
    public Genre toDao(GenreCreateDto genreCreateDto) {
        return Genre.builder()
            .name(genreCreateDto.getName())
            .build();
    }

    public Genre toDao(GenreUpdateDto genreUpdateDto, Genre genre) {
        genre.setName(genreUpdateDto.getName());
        return genre;
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
            .name(genre.getName())
            .id(genre.getId())
            .build();
    }

    public List<GenreDto> toDto(Collection<Genre> genres) {
        return genres
            .stream()
            .map(this::toDto)
            .toList();
    }
}
