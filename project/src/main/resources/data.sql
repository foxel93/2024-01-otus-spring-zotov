INSERT INTO singers(fullname)
VALUES ('Singer_1'), ('Singer_2'), ('Singer_3'),
       ('Singer_4'), ('Singer_5'), ('Singer_6');

INSERT INTO genres(name)
VALUES ('Rock'), ('Pop');

INSERT INTO albums(name)
VALUES ('Album_1'), ('Album_2');

INSERT INTO songs(name)
VALUES ('Song_1'), ('Song_2');

INSERT INTO songs_genres(song_id, genre_id)
VALUES (1, 1), (1, 2), (2, 2);

INSERT INTO songs_albums(song_id, album_id)
VALUES (1, 1), (1, 2), (2, 2);

INSERT INTO songs_singers(song_id, singer_id)
VALUES (1, 1), (2, 1), (2, 2);