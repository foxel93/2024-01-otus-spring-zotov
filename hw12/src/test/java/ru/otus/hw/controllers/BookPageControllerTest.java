package ru.otus.hw.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.MvcMockHelper.builder;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.UserService;

@DisplayName("Контроллер страниц книг")
@WebMvcTest(BookPageController.class)
@Import(SecurityConfiguration.class)
public class BookPageControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.of("/books", HttpMethod.GET, 200),
            Arguments.of("/books/1", HttpMethod.GET, 200),
            Arguments.of("/books/add", HttpMethod.GET, 200),
            Arguments.of("/books/1/edit", HttpMethod.GET, 200),
            Arguments.of("/books/1/delete", HttpMethod.POST, 302)
        );
    }

    @DisplayName("Получение ответа с авторизацией")
    @ParameterizedTest(name = "{index} - url: [{0}], http method: [{1}]")
    @MethodSource("parameters")
    @WithMockUser(username = "admin")
    void withAuth(String url, HttpMethod method, int responseStatus) throws Exception {
        mvc.perform(builder(url, method))
            .andExpect(status().is(responseStatus));
    }

    @DisplayName("Получение ответа без авторизацией")
    @ParameterizedTest(name = "{index} - url: [{0}], http method: [{1}]")
    @MethodSource("parameters")
    void withoutAuth(String url, HttpMethod method) throws Exception {
        mvc.perform(builder(url, method))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/login"));
    }
}
