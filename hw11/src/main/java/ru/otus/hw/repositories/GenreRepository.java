package ru.otus.hw.repositories;

import java.util.Set;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findByIdIn(Set<String> ids);
}
