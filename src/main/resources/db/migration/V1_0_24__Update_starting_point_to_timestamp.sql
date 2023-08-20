ALTER TABLE sprints
ALTER COLUMN actual_start_date TYPE timestamp USING actual_start_date::timestamp,
ALTER COLUMN actual_end_date TYPE timestamp USING actual_end_date::timestamp;
