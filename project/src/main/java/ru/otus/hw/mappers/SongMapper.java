package ru.otus.hw.mappers;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.AlbumDao;
import ru.otus.hw.dao.GenreDao;
import ru.otus.hw.dao.SingerDao;
import ru.otus.hw.dao.SongDao;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;

@Component
@AllArgsConstructor
public class SongMapper {
    private final SingerMapper singerMapper;

    private final AlbumMapper albumMapper;

    private final GenreMapper genreMapper;

    public SongDao toDao(SongCreateDto songCreateDto, SingerDao singerDao, AlbumDao albumDao, GenreDao genreDao) {
        return SongDao.builder()
            .name(songCreateDto.getName())
            .album(albumDao)
            .singer(singerDao)
            .genre(genreDao)
            .build();
    }

    public SongDao toDao(SongUpdateDto songUpdateDto, SongDao foundSongDao) {
        return toDao(
            songUpdateDto,
            foundSongDao.getId(),
            foundSongDao.getSinger(),
            foundSongDao.getAlbum(),
            foundSongDao.getGenre()
        );
    }

    public SongDao toDao(
        SongUpdateDto songUpdateDto,
        long id, SingerDao singerDao,
        AlbumDao albumDao,
        GenreDao genreDao
    ) {
        return SongDao.builder()
            .name(songUpdateDto.getName())
            .album(albumDao)
            .singer(singerDao)
            .genre(genreDao)
            .id(id)
            .build();
    }

    public List<SongDto> toDtoList(Collection<SongDao> songDaoList) {
        return songDaoList.stream().map(this::toDto).toList();
    }

    public SongDto toDto(SongDao songDao) {
        return SongDto.builder()
            .name(songDao.getName())
            .id(songDao.getId())
            .singer(singerMapper.toDto(songDao.getSinger()))
            .album(albumMapper.toDto(songDao.getAlbum()))
            .genre(genreMapper.toDto(songDao.getGenre()))
            .build();
    }
}
