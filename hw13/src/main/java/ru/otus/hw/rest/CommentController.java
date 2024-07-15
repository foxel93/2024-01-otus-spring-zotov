package ru.otus.hw.rest;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

@RestController
@RequestMapping("api/v1/book/{book_id}/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<CommentDto> getByBookId(@PathVariable("book_id") long bookId) {
        return commentService.findByBookId(bookId);
    }

    @GetMapping("/{comment_id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public CommentDto getById(@PathVariable("book_id") long bookId,
                              @PathVariable("comment_id") long commentId) {
        return commentService.findById(commentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public CommentDto create(@PathVariable("book_id") long bookId,
                             @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @PatchMapping("/{comment_id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public CommentDto update(@PathVariable("book_id") long bookId,
                             @PathVariable("comment_id") long commentId,
                             @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteByBookId(@PathVariable("book_id") long bookId) {
        commentService.deleteById(bookId);
    }

    @DeleteMapping("/{comment_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("book_id") long bookId,
                           @PathVariable("comment_id") long id) {
        commentService.deleteById(id);
    }
}
