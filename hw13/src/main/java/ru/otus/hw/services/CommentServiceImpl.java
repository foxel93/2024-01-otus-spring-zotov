package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public CommentDto findById(long id) {
        return commentRepository.findById(id)
            .map(commentMapper::toCommentDto)
            .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long id) {
        return commentRepository.findByBookId(id)
            .stream()
            .map(commentMapper::toCommentDto)
            .toList();
    }

    @Override
    @Transactional
    public CommentDto create(CommentCreateDto commentCreateDto) {
        var bookId = commentCreateDto.getBookId();
        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = commentMapper.toCommentDto(commentCreateDto, book);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(CommentUpdateDto commentUpdateDto) {
        var id = commentUpdateDto.getId();
        var comment = commentRepository.findById(id)
            .map(c -> commentMapper.toCommentDto(commentUpdateDto, c.getBook()))
            .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id)));
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByBookId(long id) {
        commentRepository.deleteByBookId(id);
    }
}
