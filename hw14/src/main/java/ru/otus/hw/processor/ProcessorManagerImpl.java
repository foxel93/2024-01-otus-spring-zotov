package ru.otus.hw.processor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.jpa.AuthorJpaDao;
import ru.otus.hw.dao.jpa.BookJapDao;
import ru.otus.hw.dao.jpa.GenreJpaDao;
import ru.otus.hw.dao.mongo.AuthorMongoDao;
import ru.otus.hw.dao.mongo.BookMongoDao;
import ru.otus.hw.dao.mongo.GenreMongoDao;

@Service
public class ProcessorManagerImpl implements ProcessorManager, CleanUpService {
    private final Map<String, Long> bookIdByKeyMap = new HashMap<>();

    private final Map<String, Long> authorIdByKeyMap = new HashMap<>();

    private final Map<String, Long> genreIdByKeyMap = new HashMap<>();

    public BookJapDao bookProcess(BookMongoDao item) {
        return BookJapDao.builder()
            .id(getId(bookIdByKeyMap, item.getId()))
            .title(item.getTitle())
            .author(authorProcess(item.getAuthor()))
            .genres(item.getGenres().stream().map(this::genreProcess).toList())
            .build();
    }

    public AuthorJpaDao authorProcess(AuthorMongoDao item) {
        var id = authorIdByKeyMap.computeIfAbsent(item.getId(), key -> authorIdByKeyMap.size() + 1L);
        return AuthorJpaDao.builder()
            .id(id)
            .fullName(item.getFullName())
            .build();
    }

    public GenreJpaDao genreProcess(GenreMongoDao item) {
        var id = genreIdByKeyMap.computeIfAbsent(item.getId(), key -> genreIdByKeyMap.size() + 1L);
        return GenreJpaDao.builder()
            .id(id)
            .name(item.getName())
            .build();
    }

    private long getId(Map<String, Long> map, String key) {
        return map.computeIfAbsent(key, ign -> genreIdByKeyMap.size() + 1L);
    }

    @Override
    public void cleanUp() {
        bookIdByKeyMap.clear();
        authorIdByKeyMap.clear();
        genreIdByKeyMap.clear();
    }
}
