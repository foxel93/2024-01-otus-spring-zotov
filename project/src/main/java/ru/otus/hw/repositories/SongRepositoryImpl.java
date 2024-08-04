package ru.otus.hw.repositories;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
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
    public Page<Song> findAllByAlbumAndGenreAndSinger(
        @Nullable Album album,
        @Nullable Genre genre,
        @Nullable Singer singer,
        Pageable pageable
    ) {
        var cb = em.getCriteriaBuilder();

        var result = result(cb, album, genre, singer, pageable);
        var count = count(cb, album, genre, singer);
        return new PageImpl<>(result, pageable, count);
    }

    private List<Song> result(
        CriteriaBuilder cb,
        @Nullable Album album,
        @Nullable Genre genre,
        @Nullable Singer singer,
        Pageable pageable
    ) {
        var cq = cb.createQuery(Song.class);
        var entityGraph = em.getEntityGraph("song-album-genre-singer-graph");

        var songRoot = cq.from(Song.class);
        var predicates = predicates(cb, songRoot, album, genre, singer);

        cq
            .where(predicates)
            .orderBy(QueryUtils.toOrders(pageable.getSort(), songRoot, cb))
        ;

        return em.createQuery(cq)
            .setHint("jakarta.persistence.fetchgraph", entityGraph)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
    }

    private long count(CriteriaBuilder cb, @Nullable Album album, @Nullable Genre genre, @Nullable Singer singer) {
        var countQuery = cb.createQuery(Long.class);
        var songRoot = countQuery.from(Song.class);
        var entityGraph = em.getEntityGraph("song-album-genre-singer-graph");

        var predicates = predicates(cb, songRoot, album, genre, singer);

        countQuery
            .select(cb.count(songRoot))
            .where(cb.and(predicates));

        return em.createQuery(countQuery)
            .setHint("jakarta.persistence.fetchgraph", entityGraph)
            .getSingleResult();
    }

    private Predicate[] predicates(
        CriteriaBuilder cb,
        Root<Song> songRoot,
        @Nullable Album album,
        @Nullable Genre genre,
        @Nullable Singer singer
    ) {
        var predicates = new ArrayList<Predicate>(3);

        tryAddPredicate(predicates, cb, songRoot.get("album"), album);
        tryAddPredicate(predicates, cb, songRoot.get("genre"), genre);
        tryAddPredicate(predicates, cb, songRoot.get("singer"), singer);

        return predicates.toArray(new Predicate[0]);
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
