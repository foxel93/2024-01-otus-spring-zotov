package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Author> findAll() {
        var query = em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        var query = em.createQuery(
            "SELECT a FROM Author a WHERE a.id = :id",
            Author.class
        );
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
