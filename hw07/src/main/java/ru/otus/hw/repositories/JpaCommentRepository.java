package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        var query = em.createQuery("SELECT c FROM Comment c WHERE c.book.id = :book_id", Comment.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        var comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
