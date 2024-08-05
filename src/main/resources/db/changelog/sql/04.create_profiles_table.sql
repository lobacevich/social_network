CREATE TABLE mes.profiles (
	id BIGSERIAL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL,
	firstname VARCHAR(23),
	lastname VARCHAR(23),
	user_id BIGINT NOT NULL UNIQUE REFERENCES mes.users(id)
);
