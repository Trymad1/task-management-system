CREATE TABLE IF NOT EXISTS public.users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);


CREATE TABLE IF NOT EXISTS public.priorities(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.statuses(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.tasks(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    author_id BIGINT NOT NULL,
    executor_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status_id BIGINT NOT NULL,
    priority_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (executor_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES statuses (id) ON DELETE NO ACTION,
    FOREIGN KEY (priority_id) REFERENCES priorities (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.comments(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    author_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    text VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.roles(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.user_roles(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY(role_id) REFERENCES roles (id) ON DELETE NO ACTION
);