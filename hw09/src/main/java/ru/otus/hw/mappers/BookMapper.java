package ru.otus.hw.mappers;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Component
@AllArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toBookDto(Book book) {
        return new BookDto(
            book.getId(),
            book.getTitle(),
            authorMapper.toAuthorDto(book.getAuthor()),
            book.getGenres().stream().map(genreMapper::toGenreDto).toList()
        );
    }

    public BookEditDto toBookEditDto(Book book) {
        return new BookEditDto(
            book.getId(),
            book.getTitle(),
            book.getAuthor().getId(),
            book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );
    }
}
