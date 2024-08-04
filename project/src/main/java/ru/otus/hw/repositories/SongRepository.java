package ru.otus.hw.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Song;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {
    @EntityGraph("song-album-genre-singer-graph")
    Page<Song> findAll(Pageable pageable);

    @EntityGraph("song-album-genre-singer-graph")
    Optional<Song> findById(long id);
}
