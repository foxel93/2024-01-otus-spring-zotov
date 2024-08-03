package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
import ru.otus.hw.models.Song;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;

@Component
@AllArgsConstructor
public class SongMapper {
    private final SingerMapper singerMapper;

    private final AlbumMapper albumMapper;

    private final GenreMapper genreMapper;

    public Song toDao(SongCreateDto songCreateDto, Singer singer, Album album, Genre genre) {
        return Song.builder()
            .name(songCreateDto.getName())
            .album(album)
            .singer(singer)
            .genre(genre)
            .build();
    }

    public Song toDao(SongUpdateDto songUpdateDto, Song foundSong) {
        return toDao(
            songUpdateDto,
            foundSong.getId(),
            foundSong.getSinger(),
            foundSong.getAlbum(),
            foundSong.getGenre()
        );
    }

    public Song toDao(
        SongUpdateDto songUpdateDto,
        long id, Singer singer,
        Album album,
        Genre genre
    ) {
        return Song.builder()
            .name(songUpdateDto.getName())
            .album(album)
            .singer(singer)
            .genre(genre)
            .id(id)
            .build();
    }

    public List<SongDto> toDtoList(Collection<Song> songList) {
        return songList.stream().map(this::toDto).toList();
    }

    public SongDto toDto(Song song) {
        return SongDto.builder()
            .name(song.getName())
            .id(song.getId())
            .singer(singerMapper.toDto(song.getSinger()))
            .album(albumMapper.toDto(song.getAlbum()))
            .genre(genreMapper.toDto(song.getGenre()))
            .build();
    }
}
