package ru.otus.hw.rest;

import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.handler.AuthorHandler;
import ru.otus.hw.handler.BookHandler;
import ru.otus.hw.handler.CommentHandler;
import ru.otus.hw.handler.GenreHandler;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.router.AuthorRouter;
import ru.otus.hw.router.BookRouter;
import ru.otus.hw.router.CommentRouter;
import ru.otus.hw.router.GenreRouter;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

@DisplayName("Контроллер книг")
@WebFluxTest({BookHandler.class, BookRouter.class, AuthorRouter.class, CommentRouter.class, GenreRouter.class})
@AutoConfigureDataMongo
@Import({AuthorMapper.class, GenreMapper.class, BookMapper.class, AuthorHandler.class, CommentHandler.class, GenreHandler.class})
class BookHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    @MockBean
    private CommentServiceImpl commentService;

    @DisplayName("Получение всех книг")
    @Test
    void getBooks() {
        var book = generateBook("1");
        var bookDto = bookMapper.toBookDto(book);

        given(bookService.findAll()).willReturn(Flux.just(bookDto));

        webTestClient.get().uri("/api/v1/book")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BookDto.class).hasSize(1).contains(bookDto);
    }

    @DisplayName("Получение книги по id")
    @Test
    void getBookById() {
        var id = "1";
        var book = generateBook(id);
        var bookDto = bookMapper.toBookDto(book);

        given(bookService.findById(id)).willReturn(Mono.just(bookDto));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BookDto.class).contains(bookDto);
    }

    @DisplayName("Получение 404 если книга не найдена")
    @Test
    void getNotFoundExceptionByGetting() {
        var id = "1";
        given(bookService.findById(id)).willReturn(Mono.error(new NotFoundException("")));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @DisplayName("Получение 500 если произошла ошибка при получении книги")
    @Test
    void getInternalExceptionByGetting() {
        var id = "1";
        given(bookService.findById(id)).willReturn(Mono.error(new RuntimeException()));

        webTestClient.get().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @DisplayName("Добавление новой книги")
    @Test
    void addBook() {
        var book = generateBook("10");
        var bookDto = bookMapper.toBookDto(book);
        var bookCreateDto = bookMapper.toBookCreateDto(book);

        given(bookService.create(bookCreateDto)).willReturn(Mono.just(bookDto));

        webTestClient.post().uri("/api/v1/book")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(bookCreateDto)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(BookDto.class).isEqualTo(bookDto);
    }

    @DisplayName("Изменение книги")
    @Test
    void updateBook() {
        var id = "1";
        var book = generateBook(id);
        var bookDto = bookMapper.toBookDto(book);
        var bookUpdatedDto = bookMapper.toBookUpdateDto(book);

        given(bookService.findById(bookUpdatedDto.getId())).willReturn(Mono.just(bookDto));
        given(bookService.update(bookUpdatedDto)).willReturn(Mono.just(bookDto));

        webTestClient.patch().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(bookMapper.toBookUpdateDto(book))
            .exchange()
            .expectStatus().isOk()
            .expectBody(BookDto.class).isEqualTo(bookDto);
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() {
        var id = "1";

        given(bookService.deleteById(id)).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/book/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
    }

    private Book generateBook(String id) {
        var author = new Author("1", "some_author");
        var genre = new Genre("1", "some_genre");

        given(authorService.findAll()).willReturn(Flux.just(authorMapper.toAuthorDto(author)));
        given(genreService.findAll()).willReturn(Flux.just(genreMapper.toGenreDto(genre)));

        return new Book(id, "some_title", author, List.of(genre));
    }
}