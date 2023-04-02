CREATE TABLE IF NOT EXISTS users(
    id uuid NOT NULL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    phone varchar(50) NOT NULL UNIQUE,
    email varchar(50) NOT NULL UNIQUE,
    position varchar(100) NOT NULL,
    "password" varchar(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at timestamp(0) NULL,
	updated_at timestamp(0) NULL,
	deleted_at timestamp(0) NULL
);