ALTER TABLE epics
ADD COLUMN "key" varchar(20) NOT NULL,
ADD CONSTRAINT unique_epics_key
UNIQUE ("key");
