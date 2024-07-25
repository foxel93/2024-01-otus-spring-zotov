package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dao.AlbumDao;

public interface AlbumRepository extends JpaRepository<AlbumDao, Long> {
}
