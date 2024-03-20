package ru.otus.hw.commands;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
            .map(commentConverter::commentToString)
            .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all comments by book id", key = "acbid")
    public String findAllCommentsByBookId(long id) {
        return commentService.findByBookId(id)
            .stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // bins newComment 1 1
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String commentText, long bookId) {
        var savedComment = commentService.insert(commentText, bookId);
        return commentConverter.commentToString(savedComment);
    }

    // bupd 4 editedComment 3 2
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String commentText) {
        var updatedComment = commentService.update(id, commentText);
        return commentConverter.commentToString(updatedComment);
    }

    // bdel 4
    @ShellMethod(value = "Delete Comment by id", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
