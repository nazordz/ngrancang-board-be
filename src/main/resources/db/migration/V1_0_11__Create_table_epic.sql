CREATE TABLE IF NOT EXISTS epics (
    id uuid NOT NULL PRIMARY KEY,
    project_id uuid NOT NULL,
    summary text NOT NULL,
    user_id uuid NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);