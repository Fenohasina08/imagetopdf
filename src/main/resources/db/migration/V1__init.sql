CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS image_metadata (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name VARCHAR(500) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    original_bucket_key VARCHAR(500) NOT NULL,
    transformed_bucket_key VARCHAR(500) NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_image_metadata_email ON image_metadata (email);