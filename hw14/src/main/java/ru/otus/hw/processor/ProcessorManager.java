package ru.otus.hw.processor;

import ru.otus.hw.dao.jpa.AuthorJpaDao;
import ru.otus.hw.dao.jpa.BookJapDao;
import ru.otus.hw.dao.jpa.GenreJpaDao;
import ru.otus.hw.dao.mongo.AuthorMongoDao;
import ru.otus.hw.dao.mongo.BookMongoDao;
import ru.otus.hw.dao.mongo.GenreMongoDao;

public interface ProcessorManager {
    BookJapDao bookProcess(BookMongoDao bookMongoDao);

    GenreJpaDao genreProcess(GenreMongoDao genreMongoDao);

    AuthorJpaDao authorProcess(AuthorMongoDao authorMongoDao);
}
