package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

@RestController
@RequestMapping("api/v1/book")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public Flux<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<BookDto> get(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @PostMapping
    public Mono<BookDto> create(@RequestBody @Valid BookCreateDto bookCreateDto) {
        return bookService.create(bookCreateDto);
    }

    @PatchMapping("/{id}")
    public Mono<BookDto> update(@RequestBody @Valid BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return bookService.deleteById(id);
    }
}
