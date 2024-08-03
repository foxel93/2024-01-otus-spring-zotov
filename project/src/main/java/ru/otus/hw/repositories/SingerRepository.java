package ru.otus.hw.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Singer;

public interface SingerRepository extends JpaRepository<Singer, Long> {
    Optional<Singer> findByFullname(String singer);
}
