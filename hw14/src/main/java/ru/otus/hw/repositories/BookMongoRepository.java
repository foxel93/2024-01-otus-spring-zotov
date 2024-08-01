package ru.otus.hw.repositories;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.dao.mongo.BookMongoDao;

@NonNullApi
public interface BookMongoRepository extends MongoRepository<BookMongoDao, String> {
}
