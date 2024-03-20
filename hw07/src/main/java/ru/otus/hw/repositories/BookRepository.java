package ru.otus.hw.repositories;

import io.micrometer.common.lang.NonNullApi;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;

@NonNullApi
public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(value = "book-author-genres-entity-graph")
    Optional<Book> findById(Long aLong);

    @EntityGraph(value = "book-author-entity-graph")
    @Override
    List<Book> findAll();
}
