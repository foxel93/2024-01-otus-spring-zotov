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
    private static final String FIND_ALL_QUERY =
        """
            SELECT
                id,
                name
            FROM
                genres
        """;

    private static final String FIND_BY_IDS_QUERY =
        """
            SELECT
                id,
                name
            FROM
                genres
            WHERE
                id IN (:ids)
        """;

    private static final RowMapper<Genre> MAPPER = (rs, rowNum) -> new Genre(
        rs.getInt("id"),
        rs.getString("name")
    );

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, MAPPER);
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return jdbc.query(FIND_BY_IDS_QUERY, new MapSqlParameterSource("ids", ids), MAPPER);
    }
}
