package ru.otus.hw.services;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toBookDto)
            .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(bookMapper::toBookDto)
            .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        var author = author(bookCreateDto.getAuthorId());
        var genres = genreList(bookCreateDto.getGenreIds());
        var book = bookMapper.toBook(bookCreateDto, author, genres);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        var id = bookUpdateDto.getId();
        bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
        var author = author(bookUpdateDto.getAuthorId());
        var genres = genreList(bookUpdateDto.getGenreIds());
        var book = bookMapper.toBook(bookUpdateDto, author, genres);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author author(long id) {
        return authorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(id)));
    }

    private List<Genre> genreList(Set<Long> ids) {
        var genres = genreRepository.findByIdIn(ids);
        if (ids.size() != genres.size()) {
            throw new NotFoundException("One or all genres with ids %s not found".formatted(ids));
        }
        return genres;
    }
}
