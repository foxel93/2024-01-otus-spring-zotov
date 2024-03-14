package ru.otus.hw.repositories;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        var entityGraph = em.getEntityGraph("book-entity-graph");
        var query = em.createQuery("SELECT b FROM Book b WHERE b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), entityGraph);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = em.getEntityGraph("book-entity-graph");
        var query = em.createQuery("SELECT b FROM Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
