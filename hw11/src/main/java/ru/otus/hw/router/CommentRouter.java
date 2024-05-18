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
import ru.otus.hw.handler.CommentHandler;

@Component
public class CommentRouter {
    @Bean
    public RouterFunction<ServerResponse> commentRouterFunction(CommentHandler commentHandler) {
        return nest(
            RequestPredicates.path("/api/v1/book/{book_id}/comment"),
            route(GET(""), commentHandler::allCommentsByBookId)
                .andRoute(GET("/{id}"), commentHandler::allCommentById)
                .andRoute(POST(""), commentHandler::saveComment)
                .andRoute(PATCH("/{id}"), commentHandler::updateComment)
                .andRoute(DELETE(""), commentHandler::deleteAllCommentsByBookId)
                .andRoute(DELETE("/{id}"), commentHandler::deleteCommentById));
    }
}
