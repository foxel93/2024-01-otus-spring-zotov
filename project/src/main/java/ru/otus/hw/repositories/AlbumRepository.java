package ru.otus.hw.repositories;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByIdIn(Set<Long> ids);
}
