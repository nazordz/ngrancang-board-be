CREATE TABLE IF NOT EXISTS roles(
    id uuid NOT NULL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    created_at timestamp(0) NULL,
	updated_at timestamp(0) NULL
);

CREATE TABLE IF NOT EXISTS user_roles(
    id uuid NOT NULL PRIMARY KEY,
    role_id uuid NOT NULL,
    user_id uuid NOT NULL,
    FOREIGN KEY (role_id) REFERENCES public.roles(id) 
    ON DELETE RESTRICT ON UPDATE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES public.users(id) 
    ON DELETE RESTRICT ON UPDATE RESTRICT
);