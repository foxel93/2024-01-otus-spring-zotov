package ru.otus.hw.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dao.UserDao;

public interface UserRepository extends JpaRepository<UserDao, Long> {
    Optional<UserDao> findByUsername(String username);

    boolean existsByUsername(String username);
}
