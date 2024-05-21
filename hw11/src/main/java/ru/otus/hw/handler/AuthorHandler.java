package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorHandler {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public Mono<ServerResponse> allAuthors(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(authorRepository.findAll().map(authorMapper::toAuthorDto), AuthorDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
