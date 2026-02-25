#!/bin/bash
set -e

# This script initializes the StreamRoom PostgreSQL database
# It ensures the user and database are properly created with correct credentials

echo "Initializing StreamRoom database..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';
    ALTER ROLE streamroom_user CREATEDB;
    GRANT ALL PRIVILEGES ON DATABASE streamroom TO streamroom_user;
    GRANT ALL PRIVILEGES ON SCHEMA public TO streamroom_user;
EOSQL

echo "Database initialization complete!"
