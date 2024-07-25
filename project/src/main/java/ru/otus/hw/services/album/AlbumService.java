package ru.otus.hw.services.album;

import java.util.List;
import ru.otus.hw.dto.album.AlbumCreateDto;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;

public interface AlbumService {
    List<AlbumDto> findAll();

    AlbumDto findById(long id);

    AlbumDto update(long id, AlbumUpdateDto albumUpdateDto);

    AlbumDto create(AlbumCreateDto albumCreateDto);

    void deleteById(long id);
}
