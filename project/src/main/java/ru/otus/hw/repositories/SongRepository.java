package ru.otus.hw.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
import ru.otus.hw.models.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByAlbum(Album album);

    List<Song> findAllByGenre(Genre genre);

    List<Song> findAllBySinger(Singer singer);
}
