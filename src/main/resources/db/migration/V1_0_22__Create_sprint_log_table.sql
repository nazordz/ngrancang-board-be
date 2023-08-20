CREATE TABLE active_sprint_logs (
	id uuid NOT NULL PRIMARY KEY,
	story_id uuid NOT NULL,
	sprint_id uuid NOT NULL,
	story_point int2 NOT NULL DEFAULT 0,
	"status" public."issue_status" NOT NULL DEFAULT 'todo'::issue_status,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NOT NULL DEFAULT now(),
    FOREIGN KEY (story_id) REFERENCES stories(id),
    FOREIGN KEY (sprint_id) REFERENCES sprints(id)
);