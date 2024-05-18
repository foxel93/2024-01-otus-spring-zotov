package ru.otus.hw.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@RestController
@RequestMapping("api/v1/genre")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Flux<GenreDto> getAll() {
        return genreService.findAll();
    }
}
