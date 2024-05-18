package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@Component
@RequiredArgsConstructor
public class AuthorHandler {
    private final AuthorService authorService;

    public Mono<ServerResponse> allAuthors(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(authorService.findAll(), AuthorDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
