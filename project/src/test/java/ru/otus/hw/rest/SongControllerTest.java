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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configurations.JwtConfiguration;
import ru.otus.hw.configurations.SecurityConfiguration;
import ru.otus.hw.dto.album.AlbumDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.song.SongCreateDto;
import ru.otus.hw.dto.song.SongDto;
import ru.otus.hw.dto.song.SongUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.song.SongService;
import ru.otus.hw.services.user.UserService;

@DisplayName("Контроллер песен")
@WebMvcTest(SongController.class)
@Import({SecurityConfiguration.class, JwtConfiguration.class})
public class SongControllerTest {
    private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SongService songService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @DisplayName("Получение всех песен")
    @Test
    @WithMockUser(username = "admin")
    void getSongs() throws Exception {
        var songDto = generateSong(1);
        given(songService.findAll()).willReturn(List.of(songDto));

        mvc.perform(get("/api/v1/songs")).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если песня не найдена")
    @Test
    @WithMockUser(username = "admin")
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(songService.findById(id)).willThrow(new NotFoundException(""));

        mvc.perform(get("/api/v1/songs/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Получение 500 если произошла ошибка при получении песни")
    @Test
    @WithMockUser(username = "admin")
    void getInternalExceptionByGetting() throws Exception {
        var id = 1;
        given(songService.findById(id)).willThrow(new RuntimeException());

        mvc.perform(get("/api/v1/songs/{id}", id)).andExpect(status().is5xxServerError());
    }

    @DisplayName("Добавление новой песни")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addSong() throws Exception {
        var songCreateDto = SongCreateDto.builder()
            .albumId(1L)
            .genreId(1L)
            .singerId(1L)
            .name("new_song")
            .build();
        mvc.perform(post("/api/v1/songs")
                .content(WRITER.writeValueAsString(songCreateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Изменение песни")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateSong() throws Exception {
        var id = 1;
        var songDto = generateSong(id);
        var songUpdateDto = SongUpdateDto.builder()
            .name("updated_song")
            .albumId((long) id)
            .genreId((long) id)
            .singerId((long) id)
            .build();

        given(songService.findById(id)).willReturn(songDto);

        mvc.perform(patch("/api/v1/songs/{id}", id)
                .content(WRITER.writeValueAsString(songUpdateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Удаление песни")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteSongById() throws Exception {
        var id = 1;

        mvc.perform(delete("/api/v1/songs/{id}", id))
            .andExpect(status().isOk());
    }

    private static SongDto generateSong(long id) {
        return SongDto.builder()
            .id(id)
            .name("Song_" + id)
            .album(AlbumDto.builder().id(id).name("Album_" + id).build())
            .genre(GenreDto.builder().id(id).name("Genre_" + id).build())
            .singer(SingerDto.builder().id(id).fullname("Singer_" + id).build())
            .build();
    }
}