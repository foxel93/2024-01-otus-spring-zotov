package ru.otus.hw.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

@DisplayName("Контроллер книг")
@WebMvcTest(BookController.class)
@Import({AuthorMapper.class, GenreMapper.class, BookMapper.class})
class BookControllerTest {
    private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

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
        var bookDto = bookMapper.toBookDto(book);

        given(bookService.findAll()).willReturn(List.of(bookDto));

        mvc.perform(get("/api/v1/book")).andExpect(status().isOk());
    }

    @DisplayName("Получение книги по id")
    @Test
    void getBookById() throws Exception {
        var id = 1;
        var book = generateBook(id);
        var bookDto = bookMapper.toBookDto(book);

        given(bookService.findById(id)).willReturn(bookDto);

        mvc.perform(get("/api/v1/book/{id}", id)).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если книга не найдена")
    @Test
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(bookService.findById(id)).willThrow(new NotFoundException(""));

        mvc.perform(get("/api/v1/book/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Получение 500 если произошла ошибка при получении книги")
    @Test
    void getInternalExceptionByGetting() throws Exception {
        var id = 1;
        given(bookService.findById(id)).willThrow(new RuntimeException());

        mvc.perform(get("/api/v1/book/{id}", id)).andExpect(status().is5xxServerError());
    }

    @DisplayName("Добавление новой книги")
    @Test
    void addBook() throws Exception {
        var book = generateBook(1);
        var bookDto = bookMapper.toBookCreateDto(book);

        mvc.perform(post("/api/v1/book")
                .content(WRITER.writeValueAsString(bookDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Изменение книги")
    @Test
    void updateBook() throws Exception {
        var id = 1;
        var book = generateBook(id);
        var bookDto = bookMapper.toBookDto(book);
        var bookUpdateDto = bookMapper.toBookUpdateDto(book);

        given(bookService.findById(book.getId())).willReturn(bookDto);

        mvc.perform(patch("/api/v1/book/{id}", id)
                .content(WRITER.writeValueAsString(bookUpdateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() throws Exception {
        var id = 1;

        mvc.perform(delete("/api/v1/book/{id}", id))
            .andExpect(status().isOk());
    }

    private Book generateBook(long id) {
        var author = new Author(1L, "some_author");
        var genre = new Genre(1L, "some_genre");

        given(authorService.findAll()).willReturn(List.of(authorMapper.toAuthorDto(author)));
        given(genreService.findAll()).willReturn(List.of(genreMapper.toGenreDto(genre)));

        return new Book(id, "some_title", author, List.of(genre));
    }
}