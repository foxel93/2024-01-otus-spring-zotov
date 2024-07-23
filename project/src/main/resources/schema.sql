CREATE TABLE if NOT EXISTS users (
    id bigserial,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20),
    PRIMARY KEY (id)
);