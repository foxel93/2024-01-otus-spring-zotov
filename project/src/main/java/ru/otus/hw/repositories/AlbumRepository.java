package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
