package ru.otus.hw.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RestController
@RequestMapping("api/v1/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDto> getAll() {
        return authorService.findAll();
    }
}