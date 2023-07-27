ALTER TABLE stories
DROP CONSTRAINT stories_epic_id_fkey;

ALTER TABLE stories
ADD CONSTRAINT epic_id_fkey
FOREIGN KEY(epic_id) REFERENCES epics(id);