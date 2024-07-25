package ru.otus.hw.rest;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.genre.GenreCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;
import ru.otus.hw.services.genre.GenreService;

@RestController
@RequestMapping("api/v1/genres")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public GenreDto getById(@PathVariable("id") long id) {
        return genreService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GenreDto create(@RequestBody @Valid GenreCreateDto genreCreateDto) {
        return genreService.create(genreCreateDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GenreDto update(@PathVariable long id, @RequestBody @Valid GenreUpdateDto genreUpdateDto) {
        return genreService.update(id, genreUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") long id) {
        genreService.deleteById(id);
    }
}
