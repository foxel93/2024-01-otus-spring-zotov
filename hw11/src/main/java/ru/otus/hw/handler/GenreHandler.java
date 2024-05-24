package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class GenreHandler {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public Mono<ServerResponse> allGenres(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(genreRepository.findAll().map(genreMapper::toGenreDto), GenreDto.class)
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
