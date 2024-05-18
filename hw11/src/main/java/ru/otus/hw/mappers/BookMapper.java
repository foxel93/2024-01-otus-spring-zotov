package ru.otus.hw.mappers;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
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

    public BookUpdateDto toBookUpdateDto(Book book) {
        return new BookUpdateDto(
            book.getId(),
            book.getTitle(),
            book.getAuthor().getId(),
            book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );
    }

    public BookUpdateDto toBookUpdateDto(BookDto bookDto) {
        return new BookUpdateDto(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor().getId(),
            bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
    }

    public BookCreateDto toBookCreateDto(Book book) {
        return new BookCreateDto(
            book.getTitle(),
            book.getAuthor().getId(),
            book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );
    }

    public Book toBook(BookUpdateDto bookUpdateDto, Author author, List<Genre> genres) {
        return new Book(
            bookUpdateDto.getId(),
            bookUpdateDto.getTitle(),
            author,
            genres
        );
    }

    public Book toBook(BookCreateDto bookCreateDto, Author author, List<Genre> genres) {
        return new Book(
            null,
            bookCreateDto.getTitle(),
            author,
            genres
        );
    }

    public Book toBook(BookDto bookDto) {
        return new Book(
            bookDto.getId(),
            bookDto.getTitle(),
            authorMapper.toAuthor(bookDto.getAuthor()),
            genreMapper.toGenres(bookDto.getGenres())
        );
    }
}
