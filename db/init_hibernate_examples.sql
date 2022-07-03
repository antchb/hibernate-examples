-- To run: Open psql and use command: \i FILE_PATH
-- For example:
-- /Applications/Postgres.app/Contents/Versions/14/bin/psql -U postgres
-- Enter Your Password if required. Settings for your local host you can find in 'pg_hba.conf' file
-- \i /Users/anthony/Development/github/hibernate-examples/db/init_hibernate_examples.sql

CREATE DATABASE hibernate_examples;

\c hibernate_examples

CREATE SCHEMA IF NOT EXISTS users_control;

CREATE TABLE IF NOT EXISTS users_control.fingerprints (
  fingerprint_id SERIAL PRIMARY KEY,
  data BYTEA,
  notes VARCHAR(1024),
  upd_dt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_control.users (
  user_id SERIAL PRIMARY KEY,
  first_name VARCHAR(128) NOT NULL,
  last_name VARCHAR(128) NOT NULL,
  fingerprint_id INT,
  upd_dt TIMESTAMP,

  CONSTRAINT fk_users_fingerprint_id 
    FOREIGN KEY(fingerprint_id)
      REFERENCES users_control.fingerprints(fingerprint_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS users_control.user_identities (
  identity_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  type VARCHAR(64),
  identifier VARCHAR(1024),
  upd_dt TIMESTAMP,

  CONSTRAINT fk_user_identities_user_id
    FOREIGN KEY(user_id)
      REFERENCES users_control.users(user_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS users_control.courses (
  course_id SERIAL PRIMARY KEY,
  course_name VARCHAR(128) UNIQUE NOT NULL,
  upd_dt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_control.user_courses (
  user_id INT,
  course_id INT,
  upd_dt TIMESTAMP DEFAULT CURRENT_DATE,

  PRIMARY KEY(user_id, course_id),
  
  CONSTRAINT fk_user_courses_course_id
    FOREIGN KEY(course_id)
      REFERENCES users_control.courses(course_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
      
  CONSTRAINT fk_user_courses_user_id
    FOREIGN KEY(user_id)
      REFERENCES users_control.users(user_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

-- Users + Roles
CREATE USER hibernate_examples WITH ENCRYPTED PASSWORD 'hibernate_examples';
CREATE ROLE hibernate_examples_role WITH NOLOGIN CONNECTION LIMIT -1;

GRANT ALL ON SCHEMA users_control TO hibernate_examples_role;
GRANT ALL ON ALL TABLES IN SCHEMA users_control TO hibernate_examples_role;
GRANT ALL ON ALL SEQUENCES IN SCHEMA users_control TO hibernate_examples_role;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA users_control TO hibernate_examples_role;
GRANT ALL ON ALL PROCEDURES IN SCHEMA users_control TO hibernate_examples_role;

GRANT hibernate_examples_role TO hibernate_examples;
