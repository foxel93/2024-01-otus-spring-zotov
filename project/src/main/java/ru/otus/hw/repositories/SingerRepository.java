package ru.otus.hw.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dao.SingerDao;

public interface SingerRepository extends JpaRepository<SingerDao, Long> {
    Optional<SingerDao> findByFullname(String singer);
}
