package ru.otus.hw.services.song;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
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
        return songRepository
            .findById(id)
            .map(foundSong -> songMapper.toDao(songUpdateDto, foundSong))
            .map(songRepository::save)
            .map(songMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Song with id={%d} not found".formatted(id)));
    }

    @Override
    @Transactional
    public SongDto create(SongCreateDto songCreateDto) {
        var genre = tryGetGenre(songCreateDto);
        var singer = tryGetSinger(songCreateDto);
        var album = tryGetAlbum(songCreateDto);

        var song = songMapper.toDao(songCreateDto, singer, album, genre);
        var createdSong = songRepository.save(song);
        return songMapper.toDto(createdSong);
    }

    private Genre tryGetGenre(SongCreateDto songCreateDto) {
        var genreId = songCreateDto.getGenreId();
        return genreRepository.findById(genreId)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(genreId)));
    }

    private Singer tryGetSinger(SongCreateDto songCreateDto) {
        var singerId = songCreateDto.getSingerId();
        return singerRepository.findById(singerId)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(singerId)));
    }

    private Album tryGetAlbum(SongCreateDto songCreateDto) {
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
