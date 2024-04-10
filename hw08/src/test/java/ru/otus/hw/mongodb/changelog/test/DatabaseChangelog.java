package ru.otus.hw.mongodb.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog
public class DatabaseChangelog {
    private final Map<Integer, Genre> genres = new HashMap<>();
    private final Map<Integer, Author> authors = new HashMap<>();
    private final Map<Integer, Book> books = new HashMap<>();

    @ChangeSet(order = "000", id = "dropDB", author = "vzotov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenres", author = "vzotov", runAlways = true)
    public void initGenres(GenreRepository repository) {
        IntStream.range(1, 7).forEach(i -> genres.put(i, repository.save(new Genre(Integer.toString(i),"Genre_" + i))));
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "vzotov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        IntStream.range(1, 4).forEach(i -> authors.put(i, repository.save(new Author(Integer.toString(i),"Author_" + i))));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "vzotov", runAlways = true)
    public void initBooks(BookRepository bookRepository) {
        var j = 1;
        for (var e : authors.entrySet()) {
            var i = e.getKey();
            books.put(i, bookRepository.save(Book.builder()
                .id(i.toString())
                .title("BookTitle_" + i)
                .author(authors.get(i))
                .genres(List.of(
                    genres.get(j++),
                    genres.get(j++)))
                .build()));
        }
    }

    public static <T> void initData(MongoTemplate mongoTemplate, List<T> list) {
        for (T value : list) {
            mongoTemplate.save(value);
        }
    }
}
