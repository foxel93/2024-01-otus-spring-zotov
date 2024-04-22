package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
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

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String getAll(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/books";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id, Model model) {
        var book = bookService.findById(id);
        model.addAttribute("book", book);
        return "book/book";
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book/add";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookCreateDto);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book/add";
        }
        bookService.create(bookCreateDto);
        return "redirect:/books/";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable long id, Model model) {
        var book = bookService.findById(id);
        var bookUpdateDto = bookMapper.toBookUpdateDto(book);
        model.addAttribute("book", bookUpdateDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid @ModelAttribute("book") BookUpdateDto book,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book/edit";
        }
        bookService.update(book);
        model.addAttribute("book", book);
        return "redirect:/books/{id}";
    }

    @PostMapping("{id}/delete")
    public String delete(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books/";
    }
}
