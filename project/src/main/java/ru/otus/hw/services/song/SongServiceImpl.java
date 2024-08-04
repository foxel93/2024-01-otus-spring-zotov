package ru.otus.hw.services.song;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.song.SongFindDto;
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
    public List<SongDto> findAll(Pageable pageable) {
        return songRepository.findAll(pageable)
            .stream()
            .map(songMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongDto> findAll(SongFindDto songFindDto, Pageable pageable) {
        var genre = songFindDto.getGenreId() == null ? null : tryGetGenre(songFindDto.getGenreId());
        var singer = songFindDto.getSingerId() == null ? null : tryGetSinger(songFindDto.getSingerId());
        var album = songFindDto.getAlbumId() == null ? null : tryGetAlbum(songFindDto.getAlbumId());

        return songRepository
            .findAllByAlbumAndGenreAndSinger(album, genre, singer, pageable)
            .stream()
            .map(songMapper::toDto)
            .toList();
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
        var genre = tryGetGenre(songCreateDto.getGenreId());
        var singer = tryGetSinger(songCreateDto.getSingerId());
        var album = tryGetAlbum(songCreateDto.getAlbumId());

        var song = songMapper.toDao(songCreateDto, singer, album, genre);
        var createdSong = songRepository.save(song);
        return songMapper.toDto(createdSong);
    }

    private Genre tryGetGenre(long genreId) {
        return genreRepository.findById(genreId)
            .orElseThrow(() -> new NotFoundException("Genre with id={%d} not found".formatted(genreId)));
    }

    private Singer tryGetSinger(long singerId) {
        return singerRepository.findById(singerId)
            .orElseThrow(() -> new NotFoundException("Singer with id={%d} not found".formatted(singerId)));
    }

    private Album tryGetAlbum(long albumId) {
        return albumRepository.findById(albumId)
            .orElseThrow(() -> new NotFoundException("Album with id={%d} not found".formatted(albumId)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        songRepository.deleteById(id);
    }
}
