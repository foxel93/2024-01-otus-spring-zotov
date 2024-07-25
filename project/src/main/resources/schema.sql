CREATE TABLE if NOT EXISTS users (
    id bigserial,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS singers (
    id bigserial,
    fullname VARCHAR(100) UNIQUE,
    PRIMARY KEY (id)
);