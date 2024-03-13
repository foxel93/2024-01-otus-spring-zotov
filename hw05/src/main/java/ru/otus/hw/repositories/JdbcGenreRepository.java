package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private static final RowMapper<Genre> MAPPER = (rs, rowNum) -> new Genre(
        rs.getInt("id"),
        rs.getString("name")
    );

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> findAll() {
        var sqlQuery = """
                SELECT id, name
                FROM genres
            """;
        return jdbc.query(sqlQuery, MAPPER);
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        var sqlQuery = """
                SELECT id, name
                FROM genres
                WHERE id IN (:ids)
            """;
        return jdbc.query(sqlQuery, new MapSqlParameterSource("ids", ids), MAPPER);
    }
}
