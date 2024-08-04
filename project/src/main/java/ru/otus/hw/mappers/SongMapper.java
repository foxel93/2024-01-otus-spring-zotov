package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.Set;
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

    public Song toDao(
        SongCreateDto songCreateDto,
        Collection<Singer> singers,
        Collection<Album> albums,
        Collection<Genre> genres
    ) {
        return Song.builder()
            .name(songCreateDto.getName())
            .albums(Set.copyOf(albums))
            .singers(Set.copyOf(singers))
            .genres(Set.copyOf(genres))
            .build();
    }

    public Song toDao(SongUpdateDto songUpdateDto, Song foundSong) {
        return toDao(
            songUpdateDto,
            foundSong.getId(),
            foundSong.getSingers(),
            foundSong.getAlbums(),
            foundSong.getGenres()
        );
    }

    public Song toDao(
        SongUpdateDto songUpdateDto,
        long id,
        Set<Singer> singers,
        Set<Album> albums,
        Set<Genre> genres
    ) {
        return Song.builder()
            .name(songUpdateDto.getName())
            .albums(albums)
            .singers(singers)
            .genres(genres)
            .id(id)
            .build();
    }

    public SongDto toDto(Song song) {
        return SongDto.builder()
            .name(song.getName())
            .id(song.getId())
            .singers(singerMapper.toDto(song.getSingers()))
            .albums(albumMapper.toDto(song.getAlbums()))
            .genres(genreMapper.toDto(song.getGenres()))
            .build();
    }
}
