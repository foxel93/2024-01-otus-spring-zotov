package ru.otus.hw.repositories;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Album;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Singer;
import ru.otus.hw.models.Song;

@Repository
@RequiredArgsConstructor
public class SongRepositoryImpl implements SongRepositoryCustom {
    private final EntityManager em;

    @Override
    public List<Song> findAllByAlbumAndGenreAndSinger(
        @Nullable Album album,
        @Nullable Genre genre,
        @Nullable Singer singer
    ) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Song.class);

        var song = cq.from(Song.class);
        var predicates = new ArrayList<Predicate>(3);

        tryAddPredicate(predicates, cb, song.get("album"), album);
        tryAddPredicate(predicates, cb, song.get("genre"), genre);
        tryAddPredicate(predicates, cb, song.get("singer"), singer);

        if (!predicates.isEmpty()) {
            cq.where((predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }

    private static <T> void tryAddPredicate(
        List<Predicate> predicates,
        CriteriaBuilder cb,
        Path<?> path,
        @Nullable T object
    ) {
        if (object != null) {
            predicates.add(cb.equal(path, object));
        }
    }
}
