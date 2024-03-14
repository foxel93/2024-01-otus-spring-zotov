package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Comment;

public interface CommentRepository {
    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}
