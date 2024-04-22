package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findByBookId(long id);

    CommentDto create(CommentCreateDto commentCreateDto);

    CommentDto update(CommentUpdateDto commentUpdateDto);

    void deleteById(long id);
}
