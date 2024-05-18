package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(
            author.getId(),
            author.getFullName()
        );
    }

    public Author toAuthor(AuthorDto authorDto) {
        return new Author(
            authorDto.getId(),
            authorDto.getFullName()
        );
    }
}
