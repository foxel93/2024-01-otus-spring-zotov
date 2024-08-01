package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.otus.hw.dao.mongo.AuthorMongoDao;
import ru.otus.hw.dao.mongo.BookMongoDao;
import ru.otus.hw.dao.mongo.GenreMongoDao;
import ru.otus.hw.repositories.AuthorMongoRepository;
import ru.otus.hw.repositories.BookMongoRepository;
import ru.otus.hw.repositories.GenreMongoRepository;

@ChangeLog
public class DatabaseChangelog {
    private static final List<BookData> BOOK_DATA = List.of(
        new BookData("Капитанская дочка", "А.C. Пушкин", "Классическая литература"),
        new BookData("Она написала убийство", "А. Кристи", "Детектив"),
        new BookData("Оно", "С. Кинг", "Ужасы")
    );

    @ChangeSet(order = "000", id = "dropDB", author = "vzotov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "vzotov", runAlways = true)
    public void initBooks(
        GenreMongoRepository genreRepository,
        AuthorMongoRepository authorRepository,
        BookMongoRepository bookRepository
    ) {
        var genreByName = genreByName(genreRepository);
        var authorByName = authorByName(authorRepository);

        BOOK_DATA.forEach(bookData -> {
            var genre = genreByName.get(bookData.genre);
            var author = authorByName.get(bookData.author);

            bookRepository.save(
                BookMongoDao.builder().author(author).genres(List.of(genre)).title(bookData.book).build()
            );
        });
    }

    private static Map<String, GenreMongoDao> genreByName(GenreMongoRepository genreRepository) {
        return BOOK_DATA.stream()
            .map(bookData -> genreRepository.save(GenreMongoDao.builder().name(bookData.genre).build()))
            .collect(Collectors.toMap(GenreMongoDao::getName, g -> g));
    }

    private static Map<String, AuthorMongoDao> authorByName(AuthorMongoRepository authorRepository) {
        return BOOK_DATA.stream()
            .map(bookData -> authorRepository.save(AuthorMongoDao.builder().fullName(bookData.author).build()))
            .collect(Collectors.toMap(AuthorMongoDao::getFullName, g -> g));
    }

    private record BookData(String book, String author, String genre) {
    }
}
