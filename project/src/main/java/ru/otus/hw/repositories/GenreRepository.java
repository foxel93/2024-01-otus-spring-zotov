package ru.otus.hw.repositories;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(Set<Long> ids);
}
