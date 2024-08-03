package ru.otus.hw.mappers;

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

    public Genre toDao(GenreUpdateDto genreUpdateDto, long id) {
        return Genre.builder()
            .name(genreUpdateDto.getName())
            .id(id)
            .build();
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
            .name(genre.getName())
            .id(genre.getId())
            .build();
    }
}
