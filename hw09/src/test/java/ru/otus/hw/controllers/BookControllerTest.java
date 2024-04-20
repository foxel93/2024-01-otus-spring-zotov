package ru.otus.hw.controllers;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер книг")
@WebMvcTest(BookController.class)
@Import({AuthorMapper.class, GenreMapper.class, BookMapper.class})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    @DisplayName("Получение всех книг")
    @Test
    void getBooks() throws Exception {
        var book = generateBook(1);

        given(bookService.findAll()).willReturn(List.of(book));

        mvc.perform(get("/books/")).andExpect(status().isOk());
    }

    @DisplayName("Получение книги по id")
    @Test
    void getBookById() throws Exception {
        var id = 1;
        var book = generateBook(id);

        given(bookService.findById(id)).willReturn(Optional.of(book));

        mvc.perform(get("/books/{id}", id)).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если книга не найдена")
    @Test
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(bookService.findById(id)).willThrow(new EntityNotFoundException(""));

        mvc.perform(get("/books/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Добавление новой книги")
    @Test
    void addBook() throws Exception {
        var book = generateBook(1);
        var bookDto = bookMapper.toBookEditDto(book);

        mvc.perform(post("/books/add").flashAttr("book", bookDto))
            .andExpect(redirectedUrl("/books/"));
    }

    @DisplayName("Изменение книги")
    @Test
    void updateBook() throws Exception {
        var id = 1;
        var book = generateBook(id);
        var bookDto = bookMapper.toBookEditDto(book);

        given(bookService.findById(book.getId())).willReturn(Optional.of(book));

        mvc.perform(post("/books/{id}/edit", id).flashAttr("book", bookDto))
            .andExpect(redirectedUrl("/books/" + id));
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() throws Exception {
        var id = 1;

        mvc.perform(post("/books/{id}/delete", id))
            .andExpect(redirectedUrl("/books/"));
    }

    private Book generateBook(long id) {
        var author = new Author(1L, "some_author");
        var genre = new Genre(1L, "some_genre");

        given(authorService.findAll()).willReturn(List.of(author));
        given(genreService.findAll()).willReturn(List.of(genre));

        return new Book(id, "some_title", author, List.of(genre));
    }
}