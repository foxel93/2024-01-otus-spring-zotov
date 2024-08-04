CREATE TABLE if NOT EXISTS users (
    id BIGSERIAL,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS singers (
    id BIGSERIAL,
    fullname VARCHAR(100) UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS genres (
    id BIGSERIAL,
    name VARCHAR(50) UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS albums (
    id BIGSERIAL,
    name VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS songs (
    id BIGSERIAL,
    name VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS songs_albums (
    song_id BIGINT REFERENCES songs(id) ON DELETE CASCADE,
    album_id BIGINT REFERENCES albums(id) ON DELETE CASCADE,
    PRIMARY KEY (song_id, album_id)
);

CREATE TABLE if NOT EXISTS songs_singers (
    song_id BIGINT REFERENCES songs(id) ON DELETE CASCADE,
    singer_id BIGINT REFERENCES singers(id) ON DELETE CASCADE,
    PRIMARY KEY (song_id, singer_id)
);

CREATE TABLE if NOT EXISTS songs_genres (
    song_id BIGINT REFERENCES songs(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (song_id, genre_id)
);
