package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.GenreDao;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;

@Component
@AllArgsConstructor
public class GenreMapper {
    public GenreDao toDao(GenreCreateDto genreCreateDto) {
        return GenreDao.builder()
            .name(genreCreateDto.getName())
            .build();
    }

    public GenreDao toDao(GenreUpdateDto genreUpdateDto, long id) {
        return GenreDao.builder()
            .name(genreUpdateDto.getName())
            .id(id)
            .build();
    }

    public GenreDto toDto(GenreDao genreDao) {
        return GenreDto.builder()
            .name(genreDao.getName())
            .id(genreDao.getId())
            .build();
    }
}
