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
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.album.AlbumUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.album.AlbumService;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.user.UserService;

@DisplayName("Контроллер альбомов")
@WebMvcTest(AlbumController.class)
@Import({SecurityConfiguration.class, JwtConfiguration.class})
public class AlbumControllerTest {
    private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @DisplayName("Получение всех альбомов")
    @Test
    @WithMockUser(username = "admin")
    void getAlbums() throws Exception {
        var albumDto = generateAlbum(1);
        given(albumService.findAll(any())).willReturn(List.of(albumDto));

        mvc.perform(get("/api/v1/albums")).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если альбом не найден")
    @Test
    @WithMockUser(username = "admin")
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(albumService.findById(id)).willThrow(new NotFoundException(""));

        mvc.perform(get("/api/v1/albums/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Получение 500 если произошла ошибка при получении альбома")
    @Test
    @WithMockUser(username = "admin")
    void getInternalExceptionByGetting() throws Exception {
        var id = 1;
        given(albumService.findById(id)).willThrow(new RuntimeException());

        mvc.perform(get("/api/v1/albums/{id}", id)).andExpect(status().is5xxServerError());
    }

    @DisplayName("Добавление нового альбома")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addAlbum() throws Exception {
        var albumDto = generateAlbum(1);
        mvc.perform(post("/api/v1/albums")
                .content(WRITER.writeValueAsString(albumDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Изменение альбома")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateAlbum() throws Exception {
        var id = 1;
        var albumDto = generateAlbum(id);
        var albumUpdateDto = AlbumUpdateDto.builder().name("new_album").build();

        given(albumService.findById(id)).willReturn(albumDto);

        mvc.perform(patch("/api/v1/albums/{id}", id)
                .content(WRITER.writeValueAsString(albumUpdateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Удаление альбома")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAlbumById() throws Exception {
        var id = 1;

        mvc.perform(delete("/api/v1/albums/{id}", id))
            .andExpect(status().isOk());
    }

    private static AlbumDto generateAlbum(long id) {
        return AlbumDto.builder()
            .id(id)
            .name("Album_" + id)
            .build();
    }
}