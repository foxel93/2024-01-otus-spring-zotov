package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

@DisplayName("Репозиторий на основе DataJPA для работы с комментариями")
@DataJpaTest
class JpaCommentRepositoryTest {

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private CommentRepository jpaCommentRepository;

    @DisplayName("должен загружать все комментарии по id книги")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentByBookId(Comment expectedComment) {
        prepare(expectedComment);

        var bookComments = jpaCommentRepository.findByBookId(expectedComment.getBook().getId());
        assertThat(bookComments).isNotEmpty();

        bookComments.forEach(actualComment -> assertAll(
            () -> assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment).as("Comments are not equals"),
            () -> assertThat(actualComment.getBook()).usingRecursiveComparison().isEqualTo(expectedComment.getBook()).as("Books are not equals")
        ));
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        prepare(expectedComment);

        assertThat(jpaCommentRepository.findById(expectedComment.getId()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldRemoveComment(Comment expectedComment) {
        prepare(expectedComment);
        assertThat(jpaCommentRepository.findById(expectedComment.getId())).isPresent();

        jpaCommentRepository.deleteById(expectedComment.getId());

        assertThat(jpaCommentRepository.findById(expectedComment.getId())).isNotPresent();
    }

    private void prepare(Comment expectedComment) {
        jpaBookRepository.save(expectedComment.getBook());
        jpaCommentRepository.save(expectedComment);
    }

    private static List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Book(id,
                "BookTitle_" + id,
                new Author(id, "Author_" + id),
                List.of(new Genre(id, "Genre_" + id))
            ))
            .toList();
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(5, 8).boxed()
            .map(id -> new Comment(id,
                "Comment_" + id,
                dbBooks.get(id - 5)
            ))
            .toList();
    }

    private static List<Comment> getDbComments() {
        var dbBooks = getDbBooks();
        return getDbComments(dbBooks);
    }
}