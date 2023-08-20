ALTER TABLE active_sprint_logs 
ADD is_start bool NOT NULL DEFAULT false,
ADD is_end bool NOT NULL DEFAULT false;
