package ru.otus.hw.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.router.BookRouter;

@DisplayName("Контроллер книг")
@WebFluxTest({BookHandler.class, BookRouter.class})
@TestPropertySource(properties = "mongock.enabled=false")
@Import({AuthorMapper.class, GenreMapper.class, CommentMapper.class, BookMapper.class, AuthorHandler.class, GenreHandler.class})
class BookHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookMapper bookMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("Получение всех книг")
    @Test
    void getBooks() {
        var book = generateBook("1");
        given(bookRepository.findAll()).willReturn(Flux.just(book));

        webTestClient.get().uri("/api/v1/book")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
    }

    @DisplayName("Получение книги по id")
    @Test
    void getBookById() {
        var id = "1";
        var book = generateBook(id);

        given(bookRepository.findById(id)).willReturn(Mono.just(book));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
    }

    @DisplayName("Получение 404 если книга не найдена")
    @Test
    void getNotFoundExceptionByGetting() {
        var id = "1";
        given(bookRepository.findById(id)).willReturn(Mono.error(new NotFoundException("")));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @DisplayName("Получение 500 если произошла ошибка при получении книги")
    @Test
    void getInternalExceptionByGetting() {
        var id = "1";
        given(bookRepository.findById(id)).willReturn(Mono.error(new RuntimeException()));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @DisplayName("Добавление новой книги")
    @Test
    void addBook() {
        var book = generateBook("10");
        var bookCreateDto = bookMapper.toBookCreateDto(book);

        given(bookRepository.save(any())).willReturn(Mono.just(book));

        webTestClient.post().uri("/api/v1/book")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(bookCreateDto)
            .exchange()
            .expectStatus().isCreated();
    }

    @DisplayName("Изменение книги")
    @Test
    void updateBook() {
        var id = "1";
        var book = generateBook(id);
        var bookUpdatedDto = bookMapper.toBookUpdateDto(book);

        given(bookRepository.findById(book.getId())).willReturn(Mono.just(book));
        given(bookRepository.save(any())).willReturn(Mono.just(book));

        webTestClient.patch().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(bookUpdatedDto)
            .exchange()
            .expectStatus().isOk();
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() {
        var id = "1";

        given(bookRepository.deleteById(id)).willReturn(Mono.empty());
        given(commentRepository.deleteAllByBookId(id)).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
    }

    private Book generateBook(String id) {
        var author = new Author("1", "Author_1");
        var genre = new Genre("1", "Genre_1");

        given(authorRepository.findAll()).willReturn(Flux.just(author));
        given(authorRepository.findById(author.getId())).willReturn(Mono.just(author));
        given(genreRepository.findAll()).willReturn(Flux.just(genre));
        given(genreRepository.findByIdIn(Set.of(genre.getId()))).willReturn(Flux.just(genre));

        return new Book(id, "some_title", author, List.of(genre));
    }
}