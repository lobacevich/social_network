CREATE TABLE mes.posts (
	id BIGSERIAL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL,
	post VARCHAR(255) NOT NULL,
	author_id BIGINT REFERENCES mes.profiles(id),
	group_id BIGINT REFERENCES mes.groups(id)
);
