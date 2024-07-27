package ru.otus.hw.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dao.AlbumDao;
import ru.otus.hw.dao.GenreDao;
import ru.otus.hw.dao.SingerDao;
import ru.otus.hw.dao.SongDao;

public interface SongRepository extends JpaRepository<SongDao, Long> {
    List<SongDao> findAllByAlbum(AlbumDao album);

    List<SongDao> findAllByGenre(GenreDao genreDao);

    List<SongDao> findAllBySinger(SingerDao singerDao);
}
