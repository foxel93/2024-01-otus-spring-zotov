package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@Component
@RequiredArgsConstructor
public class GenreHandler {
    private final GenreService genreService;

    public Mono<ServerResponse> allGenres(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(genreService.findAll(), GenreDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
