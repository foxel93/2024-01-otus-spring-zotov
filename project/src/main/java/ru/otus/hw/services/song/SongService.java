package ru.otus.hw.services.song;

import java.util.List;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongFindDto;
import ru.otus.hw.dto.song.SongUpdateDto;

public interface SongService {
    List<SongDto> findAll();

    List<SongDto> findAll(SongFindDto songFindDto);

    SongDto findById(long id);

    SongDto update(long id, SongUpdateDto songUpdateDto);

    SongDto create(SongCreateDto songCreateDto);

    void deleteById(long id);
}
