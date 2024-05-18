package ru.otus.hw.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.handler.BookHandler;

@Component
public class BookRouter {
    @Bean
    public RouterFunction<ServerResponse> bookRouterFunction(BookHandler bookHandler) {
        return nest(
            RequestPredicates.path("/api/v1/book"),
            route(GET(""), bookHandler::allBooks)
                .andRoute(GET("/{id}"), bookHandler::bookById)
                .andRoute(POST(""), bookHandler::saveBook)
                .andRoute(PATCH("/{id}"), bookHandler::updateBook)
                .andRoute(DELETE("/{id}"), bookHandler::deleteBook));
    }
}
