package ru.otus.hw.services.album;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.album.AlbumCreateDto;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AlbumMapper;
import ru.otus.hw.repositories.AlbumRepository;

@Service
@AllArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumMapper albumMapper;

    private final AlbumRepository albumRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AlbumDto> findAll() {
        return albumRepository.findAll()
            .stream()
            .map(albumMapper::toDto)
            .sorted(Comparator.comparing(AlbumDto::getName))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumDto findById(long id) {
        return albumRepository.findById(id)
            .map(albumMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Album with id={%d} not found".formatted(id)));
    }

    @Transactional
    public AlbumDto update(long id, AlbumUpdateDto albumUpdateDto) {
        albumRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Album with id={%d} not found".formatted(id)));
        var albumUpdateDao = albumMapper.toDao(albumUpdateDto, id);
        var updatedAlbumDao = albumRepository.save(albumUpdateDao);
        return albumMapper.toDto(updatedAlbumDao);
    }

    @Override
    @Transactional
    public AlbumDto create(AlbumCreateDto albumCreateDto) {
        var albumCreateDao = albumMapper.toDao(albumCreateDto);
        var createdAlbumDao = albumRepository.save(albumCreateDao);
        return albumMapper.toDto(createdAlbumDao);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        albumRepository.deleteById(id);
    }
}
