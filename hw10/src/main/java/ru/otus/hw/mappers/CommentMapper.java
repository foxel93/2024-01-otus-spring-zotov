package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Component
@AllArgsConstructor
public class CommentMapper {
    private final BookMapper bookMapper;

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getText(),
            bookMapper.toBookDto(comment.getBook())
        );
    }

    public Comment toCommentDto(CommentUpdateDto commentUpdateDto, Book book) {
        return new Comment(
            commentUpdateDto.getId(),
            commentUpdateDto.getText(),
            book
        );
    }

    public Comment toCommentDto(CommentCreateDto commentCreateDto, Book book) {
        return new Comment(
            0,
            commentCreateDto.getText(),
            book
        );
    }
}
