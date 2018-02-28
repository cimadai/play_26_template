# --- !Ups

CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  age INT NOT NULL
);

CREATE UNIQUE INDEX users__id ON users (id);

INSERT INTO users VALUES
  ('aaaaa', 'user1', 20),
  ('bbbbb', 'user2', 30),
  ('ccccc', 'user3', 25);

# --- !Downs

DROP TABLE users CASCADE;
