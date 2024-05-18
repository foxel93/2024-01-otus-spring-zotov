package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id)
            .map(commentMapper::toCommentDto)
            .switchIfEmpty(Mono.error(new NotFoundException("Comment with id %s not found".formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CommentDto> findAllByBookId(String id) {
        return commentRepository.findAllByBookId(id)
            .map(commentMapper::toCommentDto);
    }

    @Override
    @Transactional
    public Mono<CommentDto> create(CommentCreateDto commentCreateDto) {
        var bookId = commentCreateDto.getBookId();
        return bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found".formatted(bookId))))
            .map(book -> commentMapper.toComment(commentCreateDto, book))
            .flatMap(commentRepository::save)
            .map(commentMapper::toCommentDto);
    }

    @Override
    @Transactional
    public Mono<CommentDto> update(CommentUpdateDto commentUpdateDto) {
        var id = commentUpdateDto.getId();
        return commentRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Comment with id %s not found".formatted(id))))
            .map(comment -> commentMapper.toComment(commentUpdateDto, comment.getBook()))
            .flatMap(commentRepository::save)
            .map(commentMapper::toCommentDto);
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Mono<Void> deleteAllByBookId(String id) {
        return commentRepository.deleteAllByBookId(id);
    }
}
