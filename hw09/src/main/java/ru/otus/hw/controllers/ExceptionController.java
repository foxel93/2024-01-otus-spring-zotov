package ru.otus.hw.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.NotFoundException;

@ControllerAdvice
@Log4j2
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundEx(NotFoundException ignore) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleEntityNotFoundEx(Exception e) {
        log.error("Error: ", e);
        return ResponseEntity.internalServerError().build();
    }
}
