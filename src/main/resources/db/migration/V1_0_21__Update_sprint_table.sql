ALTER TABLE sprints 
ALTER COLUMN is_running SET DEFAULT false,
ALTER COLUMN is_running SET NOT NULL,
ADD COLUMN actual_start_date DATE NULL,
ADD COLUMN actual_end_date DATE NULL;