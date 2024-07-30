package ru.otus.hw.helper;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dao.jpa.BookJapDao;

public interface BookJpaRepository extends JpaRepository<BookJapDao, Long> {
}
