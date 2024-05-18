package ru.otus.hw.services;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toBookDto)
            .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found".formatted(id))));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
            .map(bookMapper::toBookDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> create(BookCreateDto bookCreateDto) {
        var author = author(bookCreateDto.getAuthorId());
        var genres = genres(bookCreateDto.getGenreIds());

        return author.zipWith(genres)
            .map(relations -> bookMapper.toBook(bookCreateDto, relations.getT1(), relations.getT2()))
            .flatMap(bookRepository::save)
            .map(bookMapper::toBookDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(BookUpdateDto bookUpdateDto) {
        var bookId = bookUpdateDto.getId();
        return bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found".formatted(bookId))))
            .flatMap(ignore -> {
                var authorMono = author(bookUpdateDto.getAuthorId());
                var genreMono = genres(bookUpdateDto.getGenreIds());
                return authorMono
                    .zipWith(genreMono)
                    .map(relations -> bookMapper.toBook(bookUpdateDto, relations.getT1(), relations.getT2()));
            })
            .flatMap(bookRepository::save)
            .map(bookMapper::toBookDto);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteAllByBookId(id)
            .then(bookRepository.deleteById(id));
    }

    private Mono<Author> author(String id) {
        return authorRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found".formatted(id))));
    }

    private Mono<List<Genre>> genres(Set<String> ids) {
        return genreRepository.findByIdIn(ids)
            .switchIfEmpty(Mono.error(new NotFoundException("Genres with ids %s not found".formatted(ids))))
            .collectList();
    }
}
