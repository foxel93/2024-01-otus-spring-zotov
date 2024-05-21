package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе DataMongo для работы с книгами")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static List<Author> dbAuthors;

    private static List<Genre> dbGenres;

    private static List<Book> dbBooks;

    @BeforeAll
    static void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @BeforeEach
    void beforeEach() {
        mongoTemplate.dropCollection(Book.class);
        for (var value: dbBooks) {
            mongoTemplate.save(value);
        }
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        StepVerifier
            .create(bookRepository.findById(expectedBook.getId()))
            .assertNext(book -> assertThat(book)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook))
            .expectComplete()
            .verify();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll().collectList().block();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = Book.builder()
            .title("BookTitle_10500")
            .author(dbAuthors.get(0))
            .genres(List.of(dbGenres.get(0), dbGenres.get(2)))
            .build();
        var savedBook = bookRepository.save(expectedBook).block();

        assertThat(savedBook).isNotNull();

        StepVerifier
            .create(bookRepository.findById(savedBook.getId()))
            .assertNext(book -> assertThat(book)
                .isNotNull()
                .matches(b -> !b.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(savedBook))
            .expectComplete()
            .verify();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book("1", "BookTitle_10500", dbAuthors.get(2),
            List.of(dbGenres.get(4), dbGenres.get(5)));

        StepVerifier
            .create(bookRepository.findById(expectedBook.getId()))
            .assertNext(book -> assertThat(book)
                .isNotNull()
                .isNotEqualTo(expectedBook))
            .expectComplete()
            .verify();

        StepVerifier
            .create(bookRepository.save(expectedBook))
            .assertNext(book -> assertThat(book)
                .matches(b -> !b.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook))
            .expectComplete()
            .verify();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var bookId = "1";
        var book = bookRepository.findById(bookId).block();
        assertThat(book).isNotNull();

        bookRepository.delete(book).block();

        assertThat(bookRepository.findById(bookId).blockOptional()).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Author(id.toString(), "Author_" + id))
            .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new Genre(id.toString(), "Genre_" + id))
            .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Book(id.toString(),
                "BookTitle_" + id,
                dbAuthors.get(id - 1),
                dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
            ))
            .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}