CREATE TABLE IF NOT EXISTS projects (
    id uuid NOT NULL PRIMARY KEY,
    "key" varchar(10) NOT NULL,
    "name" varchar(100)  NOT NULL,
    avatar varchar(255),
    "description" text,
    user_id uuid NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id),
    unique(key, name)
);