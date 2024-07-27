package ru.otus.hw.dto.song;

import lombok.Builder;
import lombok.Data;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.singer.SingerDto;

@Data
@Builder
public class SongDto {
    private long id;

    private String name;

    private AlbumDto album;

    private GenreDto genre;

    private SingerDto singer;
}
