package ru.otus.hw.services.genre;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<GenreDto> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable)
            .stream()
            .map(genreMapper::toDto)
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
        return genreRepository
            .findById(id)
            .map(genre -> genreMapper.toDao(genreUpdateDto, genre))
            .map(genreRepository::save)
            .map(genreMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(id)));
    }

    @Override
    public GenreDto create(GenreCreateDto genreCreateDto) {
        var genreCreate = genreMapper.toDao(genreCreateDto);
        var createdGenre = genreRepository.save(genreCreate);
        return genreMapper.toDto(createdGenre);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
