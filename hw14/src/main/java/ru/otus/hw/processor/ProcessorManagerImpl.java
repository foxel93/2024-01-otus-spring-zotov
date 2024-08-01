package ru.otus.hw.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.jpa.AuthorJpaDao;
import ru.otus.hw.dao.jpa.BookJapDao;
import ru.otus.hw.dao.jpa.GenreJpaDao;
import ru.otus.hw.dao.mongo.AuthorMongoDao;
import ru.otus.hw.dao.mongo.BookMongoDao;
import ru.otus.hw.dao.mongo.GenreMongoDao;

@Service
public class ProcessorManagerImpl implements ProcessorManager, CleanUpService {
    private volatile KeyProcessData bookKeyProcessData = new KeyProcessData();

    private volatile KeyProcessData authorKeyProcessData = new KeyProcessData();

    private volatile KeyProcessData genreKeyProcessData = new KeyProcessData();

    public BookJapDao bookProcess(BookMongoDao item) {
        return BookJapDao.builder()
            .id(bookKeyProcessData.getLongIdByStringKey(item.getId()))
            .title(item.getTitle())
            .author(authorProcess(item.getAuthor()))
            .genres(item.getGenres().stream().map(this::genreProcess).toList())
            .build();
    }

    public AuthorJpaDao authorProcess(AuthorMongoDao item) {
        return AuthorJpaDao.builder()
            .id(authorKeyProcessData.getLongIdByStringKey(item.getId()))
            .fullName(item.getFullName())
            .build();
    }

    public GenreJpaDao genreProcess(GenreMongoDao item) {
        return GenreJpaDao.builder()
            .id(genreKeyProcessData.getLongIdByStringKey(item.getId()))
            .name(item.getName())
            .build();
    }

    @Override
    public synchronized void cleanUp() {
        bookKeyProcessData = new KeyProcessData();
        authorKeyProcessData = new KeyProcessData();
        genreKeyProcessData = new KeyProcessData();
    }

    private static class KeyProcessData {
        private final Map<String, Long> idByKeyMap = new ConcurrentHashMap<>();

        private final AtomicLong counter = new AtomicLong();

        private long getLongIdByStringKey(String key) {
            return idByKeyMap.computeIfAbsent(key, ign -> counter.incrementAndGet());
        }
    }
}
