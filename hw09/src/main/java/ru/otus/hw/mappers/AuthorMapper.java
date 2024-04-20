package ru.otus.hw.mappers;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

public class AuthorMapper {
    public static AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(
            author.getId(),
            author.getFullName()
        );
    }
}
