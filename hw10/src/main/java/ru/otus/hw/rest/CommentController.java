package ru.otus.hw.rest;

import java.util.List;
import lombok.AllArgsConstructor;
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
@RequestMapping("api/v1/book/{id}/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getByBookId(@PathVariable("id") long bookId) {
        return commentService.findByBookId(bookId);
    }

    @PostMapping
    public CommentDto create(@PathVariable("id") long bookId, @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @PatchMapping
    public CommentDto update(@PathVariable("id") long bookId, @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @DeleteMapping
    public void deleteByBookId(@PathVariable("id") long bookId) {
        commentService.deleteById(bookId);
    }
}
