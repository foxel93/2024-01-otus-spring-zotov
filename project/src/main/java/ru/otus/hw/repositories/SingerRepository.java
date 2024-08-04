package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Singer;

public interface SingerRepository extends JpaRepository<Singer, Long> {
    Optional<Singer> findByFullname(String singer);

    List<Singer> findByIdIn(Set<Long> ids);
}
