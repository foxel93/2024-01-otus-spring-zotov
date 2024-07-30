package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.dao.mongo.AuthorMongoDao;

public interface AuthorMongoRepository extends MongoRepository<AuthorMongoDao, String> {
}
