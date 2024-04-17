package ru.otus.hw.repositories;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

@NonNullApi
public interface BookRepository extends MongoRepository<Book, String> {
}
