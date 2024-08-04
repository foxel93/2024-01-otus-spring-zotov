package ru.otus.hw.services.genre;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;

public interface GenreService {
    List<GenreDto> findAll(Pageable pageable);

    GenreDto findById(long id);

    GenreDto update(long id, GenreUpdateDto genreUpdateDto);

    GenreDto create(GenreCreateDto genreCreateDto);

    void deleteById(long id);
}
