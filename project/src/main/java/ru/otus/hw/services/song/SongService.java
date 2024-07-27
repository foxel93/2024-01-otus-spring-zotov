package ru.otus.hw.services.song;

import java.util.List;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;

public interface SongService {
    List<SongDto> findAll();

    List<SongDto> findAllByAlbum(long albumId);

    List<SongDto> findAllByGenre(long genreId);

    List<SongDto> findAllBySinger(long singerId);

    SongDto findById(long id);

    SongDto update(long id, SongUpdateDto songUpdateDto);

    SongDto create(SongCreateDto songCreateDto);

    void deleteById(long id);
}
