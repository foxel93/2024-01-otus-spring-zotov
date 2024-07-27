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
import ru.otus.hw.dto.singer.SingerDto;
import ru.otus.hw.dto.singer.SingerUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.jwt.JwtService;
import ru.otus.hw.services.singer.SingerService;
import ru.otus.hw.services.user.UserService;

@DisplayName("Контроллер исполнителей")
@WebMvcTest(SingerController.class)
@Import({SecurityConfiguration.class, JwtConfiguration.class})
public class SingerControllerTest {
    private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SingerService singerService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @DisplayName("Получение всех исполнителей")
    @Test
    @WithMockUser(username = "admin")
    void getSingers() throws Exception {
        var singerDto = generateSinger(1);
        given(singerService.findAll()).willReturn(List.of(singerDto));

        mvc.perform(get("/api/v1/singers")).andExpect(status().isOk());
    }

    @DisplayName("Получение 404 если исполнитель не найден")
    @Test
    @WithMockUser(username = "admin")
    void getNotFoundExceptionByGetting() throws Exception {
        var id = 1;
        given(singerService.findById(id)).willThrow(new NotFoundException(""));

        mvc.perform(get("/api/v1/singers/{id}", id)).andExpect(status().isNotFound());
    }

    @DisplayName("Получение 500 если произошла ошибка при получении исполнителя")
    @Test
    @WithMockUser(username = "admin")
    void getInternalExceptionByGetting() throws Exception {
        var id = 1;
        given(singerService.findById(id)).willThrow(new RuntimeException());

        mvc.perform(get("/api/v1/singers/{id}", id)).andExpect(status().is5xxServerError());
    }

    @DisplayName("Добавление нового исполнителя")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addSinger() throws Exception {
        var singerDto = generateSinger(1);
        mvc.perform(post("/api/v1/singers")
                .content(WRITER.writeValueAsString(singerDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Изменение исполнителя")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateSinger() throws Exception {
        var id = 1;
        var singerDto = generateSinger(id);
        var singerUpdateDto = SingerUpdateDto.builder().fullname("new_singer").build();

        given(singerService.findById(id)).willReturn(singerDto);

        mvc.perform(patch("/api/v1/singers/{id}", id)
                .content(WRITER.writeValueAsString(singerUpdateDto))
                .contentType("application/json"))
            .andExpect(status().isOk());
    }

    @DisplayName("Удаление исполнителя")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteSingerById() throws Exception {
        var id = 1;

        mvc.perform(delete("/api/v1/singers/{id}", id))
            .andExpect(status().isOk());
    }

    private static SingerDto generateSinger(long id) {
        return SingerDto.builder()
            .id(id)
            .fullname("Singer_" + id)
            .build();
    }
}