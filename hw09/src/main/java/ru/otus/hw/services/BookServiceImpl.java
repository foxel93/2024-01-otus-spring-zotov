package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return withGenres(bookRepository.findAll());
    }

    private List<Book> withGenres(List<Book> books) {
        for (var book : books) {
            book.getGenres().size();
        }

        return books;
    }

    @Transactional
    @Override
    public Book insert(BookEditDto dto) {
        return save(dto);
    }

    @Transactional
    @Override
    public Book update(BookEditDto bookEditDto) {
        var id = bookEditDto.getId();
        findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return save(bookEditDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(BookEditDto dto) {
        var author = authorRepository.findById(dto.getAuthorId())
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(dto.getAuthorId())));
        var genres = genreRepository.findByIdIn(dto.getGenreIds());
        if (dto.getGenreIds().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(dto.getGenreIds()));
        }

        var book = new Book(dto.getId(), dto.getTitle(), author, genres);
        return bookRepository.save(book);
    }
}
