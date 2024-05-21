package ru.otus.hw.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final Validator validator;

    public Mono<ServerResponse> bookById(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bookRepository.findById(id).map(bookMapper::toBookDto), BookDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> allBooks(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bookRepository.findAll().map(bookMapper::toBookDto), BookDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveBook(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(BookCreateDto.class).doOnNext(this::validate)
            .flatMap(bookCreateDto ->
                ServerResponse
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(save(bookCreateDto), BookDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(BookUpdateDto.class).doOnNext(this::validate)
            .flatMap(bookDto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(update(bookDto), BookDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return ServerResponse
            .status(HttpStatus.NO_CONTENT)
            .build(commentRepository.deleteAllByBookId(id).then(bookRepository.deleteById(id)))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    private <T> void validate(T obj) {
        var violations = this.validator.validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, violations.toString());
    }

    private Mono<BookDto> save(BookCreateDto bookCreateDto) {
        var author = author(bookCreateDto.getAuthorId());
        var genres = genres(bookCreateDto.getGenreIds());

        return author.zipWith(genres)
            .map(relations -> bookMapper.toBook(bookCreateDto, relations.getT1(), relations.getT2()))
            .flatMap(bookRepository::save)
            .map(bookMapper::toBookDto);
    }

    private Mono<BookDto> update(BookUpdateDto bookUpdateDto) {
        var bookId = bookUpdateDto.getId();
        return bookRepository.findById(bookId)
            .flatMap(ignore -> {
                var authorMono = author(bookUpdateDto.getAuthorId());
                var genreMono = genres(bookUpdateDto.getGenreIds());
                return authorMono
                    .zipWith(genreMono)
                    .map(relations -> bookMapper.toBook(bookUpdateDto, relations.getT1(), relations.getT2()));
            })
            .flatMap(bookRepository::save)
            .map(bookMapper::toBookDto);
    }

    private Mono<Author> author(String id) {
        return authorRepository.findById(id)
            .map(Objects::requireNonNull)
            .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found".formatted(id))));
    }

    private Mono<List<Genre>> genres(Set<String> ids) {
        return genreRepository.findByIdIn(ids)
            .switchIfEmpty(Mono.error(new NotFoundException("Genres with ids %s not found".formatted(ids))))
            .collectList();
    }
}
