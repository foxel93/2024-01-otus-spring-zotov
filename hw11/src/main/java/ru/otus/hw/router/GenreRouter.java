package ru.otus.hw.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.handler.GenreHandler;

@Component
public class GenreRouter {
    @Bean
    public RouterFunction<ServerResponse> genreRouterFunction(GenreHandler genreHandler) {
        return route(GET("/api/v1/genre"), genreHandler::allGenres);
    }
}
