package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Genre;

@DisplayName("Репозиторий на основе DataJPA для работы с жанрами")
@DataJpaTest
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository jpaGenreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        var actualGenres = jpaGenreRepository.findAllByIds(Set.of(expectedGenre.getId()));
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(List.of(expectedGenre));
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new Genre(id, "Genre_" + id))
            .toList();
    }
}