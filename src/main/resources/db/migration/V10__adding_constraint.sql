DELETE FROM users
WHERE user_id NOT IN (
    SELECT MIN(user_id)
    FROM users
    GROUP BY username
);

ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);
