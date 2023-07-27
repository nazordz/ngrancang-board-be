CREATE TYPE issue_type AS ENUM('task', 'bug', 'story', 'epic', 'sub_task');

CREATE TYPE issue_priority AS ENUM ('highest', 'high', 'medium', 'low', 'lowest');

CREATE TYPE issue_status AS ENUM('todo', 'in_progress', 'review', 'done');

CREATE TABLE IF NOT EXISTS sprints (
    id uuid NOT NULL PRIMARY KEY,
    project_id uuid NOT NULL,
    user_id uuid NOT NULL,
    sprint_name varchar(255) NOT NULL,
    "start_date" date NOT NULL,
    end_date date NOT NULL,
    sprint_goal text,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS issues (
    id uuid NOT NULL PRIMARY KEY,
    project_id uuid NOT NULL,
    sprint_id uuid,
    user_id uuid NOT NULL,
    "type" issue_type NOT NULL DEFAULT 'story',
    priority issue_priority NOT NULL DEFAULT 'medium',
    "status" issue_status NOT NULL DEFAULT 'todo',
    summary varchar(255) NOT NULL,
    attachments json,
    "description" TEXT,
    assignee uuid,
    epic_id uuid,
    story_point SMALLINT,
    created_at timestamp NOT NULL DEFAULT NOW(),
    updated_at timestamp NOT NULL DEFAULT NOW(),
    deleted_at timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (assignee) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (sprint_id) REFERENCES sprints(id),
    FOREIGN KEY (epic_id) REFERENCES issues(id)
);