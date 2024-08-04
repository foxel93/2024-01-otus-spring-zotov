package ru.otus.hw.rest;

import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configurations.JwtConfiguration;
import ru.otus.hw.configurations.SecurityConfiguration;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.genre.GenreService;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.user.UserService;

@DisplayName("Контроллер жанров")
@WebMvcTest(GenreController.class)
@Import({SecurityConfiguration.class, JwtConfiguration.class})
public class GenreControllerTest {
    private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @DisplayName("Получение всех жанров")
    @Test
    @WithMockUser(username = "admin")
    void getGenres() throws Exception {
        var genreDto = generateGenre(1);
        given(genreService.findAll(any())).willReturn(List.of(genreDto));

        mvc.perform(get("/api/v1/genres")).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если жанр не найден")
    @Test
    @WithMockUser(username = "admin")
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(genreService.findById(id)).willThrow(new NotFoundException(""));

        mvc.perform(get("/api/v1/genres/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Получение 500 если произошла ошибка при получении жанра")
    @Test
    @WithMockUser(username = "admin")
    void getInternalExceptionByGetting() throws Exception {
        var id = 1;
        given(genreService.findById(id)).willThrow(new RuntimeException());

        mvc.perform(get("/api/v1/genres/{id}", id)).andExpect(status().is5xxServerError());
    }

    @DisplayName("Добавление нового жанра")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addGenre() throws Exception {
        var genreDto = generateGenre(1);
        mvc.perform(post("/api/v1/genres")
                .content(WRITER.writeValueAsString(genreDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Изменение жанра")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateGenre() throws Exception {
        var id = 1;
        var genreDto = generateGenre(id);
        var genreUpdateDto = GenreUpdateDto.builder().name("new_genre").build();

        given(genreService.findById(id)).willReturn(genreDto);

        mvc.perform(patch("/api/v1/genres/{id}", id)
                .content(WRITER.writeValueAsString(genreUpdateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Удаление жанра")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteGenreById() throws Exception {
        var id = 1;

        mvc.perform(delete("/api/v1/genres/{id}", id))
            .andExpect(status().isOk());
    }

    private static GenreDto generateGenre(long id) {
        return GenreDto.builder()
            .id(id)
            .name("Genre_" + id)
            .build();
    }
}