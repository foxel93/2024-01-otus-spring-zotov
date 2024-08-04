package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Song;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {
}
