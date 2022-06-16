-- IMPORTANT! This script will remove entire database including 'antchb' user and related role!

-- To run: Open psql and use command: \i FILE_PATH
-- For example:
-- /Applications/Postgres.app/Contents/Versions/14/bin/psql -U postgres
-- Enter Your Password if required. Settings for your local host you can find in 'pg_hba.conf' file
-- \i /Users/anthony/Development/github/hibernate-examples/db/drop_hibernate_examples.sql

\c postgres

DROP DATABASE IF EXISTS hibernate_examples WITH (FORCE);

DROP ROLE hibernate_examples_role;
DROP USER antchb;
