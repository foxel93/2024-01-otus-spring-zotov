package ru.otus.hw.repositories;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
import ru.otus.hw.models.Song;

public interface SongRepositoryCustom {
    Page<Song> findAllByAlbumAndGenreAndSinger(
        @Nullable Album album,
        @Nullable Genre genre,
        @Nullable Singer singer,
        Pageable pageable
    );
}
