package ru.otus.hw.repositories;

import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.dao.mongo.GenreMongoDao;

public interface GenreMongoRepository extends MongoRepository<GenreMongoDao, String> {
    List<GenreMongoDao> findByIdIn(Set<String> ids);
}
