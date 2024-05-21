package ru.otus.hw.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.validation.Validator;
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
import ru.otus.hw.services.BookService;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final BookService bookService;

    private final Validator validator;

    public Mono<ServerResponse> bookById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bookService.findById(id), BookDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> allBooks(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bookService.findAll(), BookDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveBook(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BookCreateDto.class).doOnNext(this::validate)
            .flatMap(bookCreateDto ->
                ServerResponse
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookService.create(bookCreateDto), BookDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(BookUpdateDto.class).doOnNext(this::validate)
            .flatMap(bookUpdateDto ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookService.update(bookUpdateDto), BookDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse
            .status(HttpStatus.NO_CONTENT)
            .build(bookService.deleteById(id))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    private <T> void validate(T obj) {
        var violations = this.validator.validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, violations.toString());
    }
}
