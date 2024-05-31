package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@AllArgsConstructor
public class BookPageController {
    @GetMapping
    public String getAll() {
        return "book/books";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "book/book";
    }

    @GetMapping("/add")
    public String create() {
        return "book/add";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "book/edit";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "redirect:/books/";
    }
}
