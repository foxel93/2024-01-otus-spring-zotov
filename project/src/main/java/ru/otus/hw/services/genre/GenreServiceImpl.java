package ru.otus.hw.services.genre;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreMapper genreMapper;

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
            .stream()
            .map(genreMapper::toDto)
            .sorted(Comparator.comparing(GenreDto::getName))
            .toList();
    }

    @Override
    public GenreDto findById(long id) {
        return genreRepository.findById(id)
            .map(genreMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(id)));
    }

    @Override
    @Transactional
    public GenreDto update(long id, GenreUpdateDto genreUpdateDto) {
        genreRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(id)));
        var genreUpdateDao = genreMapper.toDao(genreUpdateDto, id);
        var updatedGenreDao = genreRepository.save(genreUpdateDao);
        return genreMapper.toDto(updatedGenreDao);
    }

    @Override
    public GenreDto create(GenreCreateDto genreCreateDto) {
        var genreCreateDao = genreMapper.toDao(genreCreateDto);
        var createdGenreDao = genreRepository.save(genreCreateDao);
        return genreMapper.toDto(createdGenreDao);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
