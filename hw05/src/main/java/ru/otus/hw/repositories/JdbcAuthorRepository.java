package ru.otus.hw.repositories;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {
    private static final RowMapper<Author> MAPPER = (rs, rowNum) -> new Author(
        rs.getInt("id"),
        rs.getString("full_name")
    );

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> findAll() {
        var sqlQuery = """
                SELECT id, full_name
                FROM authors
            """;
        return jdbc.query(sqlQuery, MAPPER);
    }

    @Override
    public Optional<Author> findById(long id) {
        var params = Map.<String, Object>of("id", id);
        var sqlQuery = """
                SELECT id, full_name
                FROM authors
                WHERE id = :id
            """;
        var author = jdbc.queryForObject(sqlQuery, params, MAPPER);
        return Optional.ofNullable(author);
    }
}
