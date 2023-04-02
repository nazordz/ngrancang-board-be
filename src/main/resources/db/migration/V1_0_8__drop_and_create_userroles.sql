DROP TABLE users_roles;

CREATE TABLE IF NOT EXISTS user_roles (
    id uuid NOT NULL PRIMARY KEY,
    role_id uuid NOT NULL,
    user_id uuid NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);