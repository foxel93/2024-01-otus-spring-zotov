package ru.otus.hw.services.genre;

import java.util.List;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;

public interface GenreService {
    List<GenreDto> findAll();

    GenreDto findById(long id);

    GenreDto update(long id, GenreUpdateDto genreUpdateDto);

    GenreDto create(GenreCreateDto genreCreateDto);

    void deleteById(long id);
}
