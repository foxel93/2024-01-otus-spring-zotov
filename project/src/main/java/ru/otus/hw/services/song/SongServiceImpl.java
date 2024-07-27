package ru.otus.hw.services.song;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dao.AlbumDao;
import ru.otus.hw.dao.GenreDao;
import ru.otus.hw.dao.SingerDao;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.SongMapper;
import ru.otus.hw.repositories.AlbumRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.SingerRepository;
import ru.otus.hw.repositories.SongRepository;

@Service
@AllArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;

    private final AlbumRepository albumRepository;

    private final GenreRepository genreRepository;

    private final SingerRepository singerRepository;

    private final SongMapper songMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SongDto> findAll() {
        return songRepository.findAll()
            .stream()
            .map(songMapper::toDto)
            .sorted(Comparator.comparing(SongDto::getName))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongDto> findAllByAlbum(long albumId) {
        return albumRepository.findById(albumId)
            .map(songRepository::findAllByAlbum)
            .map(songMapper::toDtoList)
            .orElseThrow(() -> new NotFoundException("Album with id={%d} not found".formatted(albumId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongDto> findAllByGenre(long genreId) {
        return genreRepository.findById(genreId)
            .map(songRepository::findAllByGenre)
            .map(songMapper::toDtoList)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(genreId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongDto> findAllBySinger(long singerId) {
        return singerRepository.findById(singerId)
            .map(songRepository::findAllBySinger)
            .map(songMapper::toDtoList)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(singerId)));
    }

    @Override
    @Transactional(readOnly = true)
    public SongDto findById(long id) {
        return songRepository
            .findById(id)
            .map(songMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Song with id={%d} not found".formatted(id)));
    }

    @Override
    @Transactional
    public SongDto update(long id, SongUpdateDto songUpdateDto) {
        var foundSongDao = songRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Song with id={%d} not found".formatted(id)));
        var songDao = songMapper.toDao(songUpdateDto, foundSongDao);
        var updatedSongDao = songRepository.save(songDao);
        return songMapper.toDto(updatedSongDao);
    }

    @Override
    @Transactional
    public SongDto create(SongCreateDto songCreateDto) {
        var genreDao = tryGetGenreDao(songCreateDto);
        var singerDao = tryGetSingerDao(songCreateDto);
        var albumDao = tryGetAlbumDao(songCreateDto);

        var songDao = songMapper.toDao(songCreateDto, singerDao, albumDao, genreDao);
        var createdSongDao = songRepository.save(songDao);
        return songMapper.toDto(createdSongDao);
    }

    private GenreDao tryGetGenreDao(SongCreateDto songCreateDto) {
        var genreId = songCreateDto.getGenreId();
        return genreRepository.findById(genreId)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(genreId)));
    }

    private SingerDao tryGetSingerDao(SongCreateDto songCreateDto) {
        var singerId = songCreateDto.getSingerId();
        return singerRepository.findById(singerId)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(singerId)));
    }

    private AlbumDao tryGetAlbumDao(SongCreateDto songCreateDto) {
        var albumId = songCreateDto.getAlbumId();
        return albumRepository.findById(albumId)
            .orElseThrow(() -> new NotFoundException("Album with id={%d} not found".formatted(albumId)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        songRepository.deleteById(id);
    }
}
