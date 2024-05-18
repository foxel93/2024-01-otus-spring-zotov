package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Flux<CommentDto> getAllByBookId(@PathVariable("book_id") String bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @GetMapping("/{comment_id}")
    public Mono<CommentDto> getById(@PathVariable("book_id") String bookId,
                                    @PathVariable("comment_id") String commentId) {
        return commentService.findById(commentId);
    }

    @PostMapping
    public Mono<CommentDto> create(@PathVariable("book_id") String bookId,
                                   @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @PatchMapping("/{comment_id}")
    public Mono<CommentDto> update(@PathVariable("book_id") String bookId,
                                   @PathVariable("comment_id") String commentId,
                                   @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @DeleteMapping
    public Mono<Void> deleteAllByBookId(@PathVariable("book_id") String bookId) {
        return commentService.deleteById(bookId);
    }

    @DeleteMapping("/{comment_id}")
    public Mono<Void> deleteById(@PathVariable("book_id") String bookId,
                                 @PathVariable("comment_id") String id) {
        return commentService.deleteById(id);
    }
}
