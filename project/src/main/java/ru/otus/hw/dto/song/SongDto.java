package ru.otus.hw.dto.song;

import java.util.List;
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

    private List<AlbumDto> albums;

    private List<GenreDto> genres;

    private List<SingerDto> singers;
}
