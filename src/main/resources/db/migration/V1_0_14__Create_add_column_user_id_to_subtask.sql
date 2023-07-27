ALTER TABLE sub_tasks
ADD COLUMN user_id uuid NULL,
ADD CONSTRAINT user_id_fkey
FOREIGN KEY(user_id) REFERENCES users(id);