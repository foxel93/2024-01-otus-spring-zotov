package ru.otus.hw.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.NotFoundException;

@RestControllerAdvice
@Log4j2
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundEx(NotFoundException ignore) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleEx(Exception e) {
        log.error("Error: ", e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<String> handleAuthorizationDeniedException(AccessDeniedException e) {
        log.error("Error: ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied");
    }
}
