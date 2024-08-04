package ru.otus.hw.repositories;

import jakarta.annotation.Nullable;
import java.util.List;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
import ru.otus.hw.models.Song;

public interface SongRepositoryCustom {
    List<Song> findAllByAlbumAndGenreAndSinger(@Nullable Album album, @Nullable Genre genre, @Nullable Singer singer);
}
