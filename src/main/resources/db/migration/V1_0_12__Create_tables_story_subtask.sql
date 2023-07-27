CREATE TABLE IF NOT EXISTS stories  (
    id uuid NOT NULL PRIMARY KEY,
    "key" varchar(20) NOT NULL UNIQUE,
    project_id uuid NOT NULL,
    user_id uuid NOT NULL,
    sprint_id uuid,
    epic_id uuid,
    priority issue_priority NOT NULL DEFAULT 'medium',
    "status" issue_status NOT NULL DEFAULT 'todo',
    summary varchar(255) NOT NULL,
    attachments json,
    "description" TEXT,
    assignee uuid,
    story_point SMALLINT,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (assignee) REFERENCES users(id),
    FOREIGN KEY (sprint_id) REFERENCES sprints(id),
    FOREIGN KEY (epic_id) REFERENCES issues(id)
);

CREATE TABLE IF NOT EXISTS sub_tasks (
    id uuid NOT NULL PRIMARY KEY,
    "key" varchar(20) NOT NULL UNIQUE,
    story_id uuid NOT NULL,
    attachments json,
    "description" TEXT,
    "status" issue_status NOT NULL DEFAULT 'todo',
    assignee uuid,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (assignee) REFERENCES users(id),
    FOREIGN KEY (story_id) REFERENCES stories(id)
);