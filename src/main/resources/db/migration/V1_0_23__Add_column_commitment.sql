ALTER TABLE sprints 
ADD plan_story_point smallint NOT NULL DEFAULT 0,
ADD actual_story_point smallint NOT NULL DEFAULT 0;
