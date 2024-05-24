package ru.otus.hw.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.handler.AuthorHandler;

@Component
@RequiredArgsConstructor
public class AuthorRouter {
    @Bean
    public RouterFunction<ServerResponse> authorRouterFunction(AuthorHandler authorHandler) {
        return route(GET("/api/v1/author"), authorHandler::allAuthors);
    }
}
