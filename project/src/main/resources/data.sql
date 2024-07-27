INSERT INTO singers(fullname)
values ('Singer_1'), ('Singer_2'), ('Singer_3'),
       ('Singer_4'), ('Singer_5'), ('Singer_6');

INSERT INTO genres(name)
values ('Rock'), ('Pop');

INSERT INTO albums(name)
values ('Album_1'), ('Album_2');

INSERT INTO songs(name, singer_id, album_id, genre_id)
values ('Song_1', 1, 1, 1), ('Song_2', 2, 2, 2);