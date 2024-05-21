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
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

@Component
@RequiredArgsConstructor
public class CommentHandler {
    private final CommentService commentService;

    private final Validator validator;

    public Mono<ServerResponse> allCommentById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("comment_id");
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(commentService.findById(id), CommentDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> allCommentsByBookId(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("book_id");
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(commentService.findAllByBookId(id), CommentDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveComment(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CommentCreateDto.class).doOnNext(this::validate)
            .flatMap(commentCreateDto ->
                ServerResponse
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentService.create(commentCreateDto), CommentDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateComment(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(CommentUpdateDto.class).doOnNext(this::validate)
            .flatMap(commentUpdateDto ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentService.update(commentUpdateDto), CommentDto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteCommentById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("comment_id");
        return ServerResponse
            .status(HttpStatus.NO_CONTENT)
            .build(commentService.deleteById(id))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAllCommentsByBookId(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("book_id");
        return ServerResponse
            .status(HttpStatus.NO_CONTENT)
            .build(commentService.deleteAllByBookId(id))
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
