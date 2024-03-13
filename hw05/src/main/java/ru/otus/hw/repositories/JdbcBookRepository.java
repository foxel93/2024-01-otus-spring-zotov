package ru.otus.hw.repositories;

import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JdbcBookRepository implements BookRepository {
    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var sqlQuery = """
                SELECT
                    b.id AS book_id,
                    b.title AS book_title,
                    a.id AS author_id,
                    a.full_name AS author_name,
                    g.id AS genre_id,
                    g.name AS genre_name
                FROM books b
                LEFT JOIN authors a ON a.id = b.author_id
                LEFT JOIN books_genres bg ON bg.book_id = b.id
                LEFT JOIN genres g ON g.id = bg.genre_id
                WHERE b.id = :book_id
            """;
        return jdbc.query(sqlQuery, mapBookIdParam(id), new BookResultSetExtractor());
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var sqlQuery = """
                DELETE FROM books
                WHERE  id = :book_id
            """;
        jdbc.update(sqlQuery, mapBookIdParam(id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        var sqlQuery = """
            SELECT
                b.id AS book_id,
                b.title AS book_title,
                a.id AS author_id,
                a.full_name AS author_name
            FROM
                books b
            LEFT JOIN
                authors a ON a.id = b.author_id
            """;
        return jdbc.query(sqlQuery, (rs, rowNum) -> mapBookRow(rs));
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        var sqlQuery = """
                SELECT book_id, genre_id
                FROM books_genres
            """;
        return jdbc.query(sqlQuery, (rs, rowNum) -> mapBookByGenreRow(rs));
    }

    private void mergeBooksInfo(
        List<Book> booksWithoutGenres,
        List<Genre> genres,
        List<BookGenreRelation> relations
    ) {
        var bookByIdMap = booksWithoutGenres.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        var genreByIdMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));

        relations.forEach(relation -> {
            var book = bookByIdMap.get(relation.bookId);
            var genre = genreByIdMap.get(relation.genreId);
            if (book != null && genre != null) {
                book.getGenres().add(genre);
            }
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sqlQuery = """
                INSERT INTO books (title, author_id)
                VALUES (:book_title, :author_id)
            """;
        jdbc.update(sqlQuery, mapBookParams(book), keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var sqlQuery = """
                UPDATE books
                SET
                    title = :book_title,
                    author_id = :author_id
                WHERE id = :book_id
            """;
        var result = jdbc.update(sqlQuery, mapBookParams(book));

        if (result == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var sqlQuery = """
                INSERT INTO books_genres (book_id, genre_id)
                VALUES (:book_id, :genre_id)
            """;
        jdbc.batchUpdate(sqlQuery, mapBookGenreRelationParams(book));
    }

    private void removeGenresRelationsFor(Book book) {
        var sqlQuery = """
                DELETE FROM books_genres
                WHERE book_id = :book_id
            """;
        jdbc.update(sqlQuery, mapBookIdParam(book));
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    book = mapBookRow(rs);
                }
                var genre = mapGenreRow(rs);
                book.getGenres().add(genre);
            }
            return Optional.ofNullable(book);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static Book mapBookRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("book_id"),
            rs.getString("book_title"),
            mapAuthorRow(rs),
            new ArrayList<>()
        );
    }

    private static Author mapAuthorRow(ResultSet rs) throws SQLException {
        return new Author(
            rs.getInt("author_id"),
            rs.getString("author_name")
        );
    }

    private static Genre mapGenreRow(ResultSet rs) throws SQLException {
        return new Genre(
            rs.getInt("genre_id"),
            rs.getString("genre_name")
        );
    }

    private static BookGenreRelation mapBookByGenreRow(ResultSet rs) throws SQLException {
        return new BookGenreRelation(
            rs.getInt("book_id"),
            rs.getInt("genre_id")
        );
    }

    private static MapSqlParameterSource mapBookParams(Book book) {
        var params = mapBookIdParam(book.getId());
        params.addValue("book_title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        return params;
    }

    private static MapSqlParameterSource mapBookIdParam(Book book) {
        return mapBookIdParam(book.getId());
    }

    private static MapSqlParameterSource mapBookIdParam(long bookId) {
        var params = new MapSqlParameterSource();
        params.addValue("book_id", bookId);
        return params;
    }

    private static SqlParameterSource[] mapBookGenreRelationParams(Book book) {
        var genres = book.getGenres();
        var bookId = book.getId();

        var params = new SqlParameterSource[genres.size()];
        for (int i = 0; i < params.length; i++) {
            var genreId = genres.get(i).getId();
            params[i] = mapBookGenreRelationParam(bookId, genreId);
        }
        return params;
    }

    private static MapSqlParameterSource mapBookGenreRelationParam(long bookId, long genreId) {
        var params = new MapSqlParameterSource();

        params.addValue("book_id", bookId);
        params.addValue("genre_id", genreId);

        return params;
    }
}
