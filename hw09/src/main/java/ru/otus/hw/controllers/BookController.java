package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller()
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper = new BookMapper();

    @GetMapping("/")
    public String allBooks(Model model) {
        var books = bookService.findAll();
        var bookDto = books.stream()
            .map(bookMapper::toBookDto)
            .toList();
        model.addAttribute("books", bookDto);
        return "book/books";
    }

    @GetMapping("/{id}")
    public String bookById(@PathVariable long id, Model model) {
        var book = bookService.findById(id)
            .map(bookMapper::toBookDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("book", book);
        return "book/book";
    }

    @GetMapping("/add")
    public String insertBook(Model model) {
        model.addAttribute("book", new BookEditDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book/add";
    }

    @PostMapping("/add")
    public String insertBook(@Valid @ModelAttribute("book") BookEditDto bookEditDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookEditDto);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book/add";
        }
        bookService.insert(bookEditDto);
        return "redirect:/books/";
    }

    @GetMapping("/{id}/edit")
    public String updateBook(@PathVariable long id, Model model) {
        var book = bookService.findById(id)
            .map(bookMapper::toBookEditDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookEditDto bookDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookDto);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book/add";
        }
        bookService.update(bookDto);
        model.addAttribute("book", bookDto);
        return "redirect:/books/{id}";
    }

    @PostMapping("{id}/delete")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books/";
    }
}
