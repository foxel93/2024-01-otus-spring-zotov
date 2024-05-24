package ru.otus.hw.mongodb.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private static final List<BookData> BOOK_DATA = List.of(
        new BookData("Капитанская дочка", "А.C. Пушкин", "Классическая литература", "Супер", "Так себе"),
        new BookData("Она написала убийство", "А. Кристи", "Детектив"),
        new BookData("Оно", "С. Кинг", "Ужасы", "Теперь плохо сплю")
    );

    @ChangeSet(order = "000", id = "dropDB", author = "vzotov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "vzotov", runAlways = true)
    public void initBooks(GenreRepository genreRepository, AuthorRepository authorRepository,
                          BookRepository bookRepository, CommentRepository commentRepository) {
        var genreByName = genreByName(genreRepository);
        var authorByName = authorByName(authorRepository);

        BOOK_DATA.forEach(bookData -> {
            var genre = genreByName.get(bookData.genre);
            var author = authorByName.get(bookData.author);

            var book = bookRepository.save(new Book(null, bookData.book, author, List.of(genre))).block();
            for (var comment : bookData.comments) {
                commentRepository.save(new Comment(null, comment, book)).block();
            }
        });
    }

    private static Map<String, Genre> genreByName(GenreRepository genreRepository) {
        return BOOK_DATA.stream()
            .map(bookData -> genreRepository.save(new Genre(null, bookData.genre)).block())
            .collect(Collectors.toMap(Genre::getName, g -> g));
    }

    private static Map<String, Author> authorByName(AuthorRepository authorRepository) {
        return BOOK_DATA.stream()
            .map(bookData -> authorRepository.save(new Author(null, bookData.author)).block())
            .collect(Collectors.toMap(Author::getFullName, g -> g));
    }

    private record BookData(String book, String author, String genre, String... comments) {
    }
}
