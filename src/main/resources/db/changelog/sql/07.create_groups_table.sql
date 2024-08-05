CREATE TABLE mes.groups (
	id BIGSERIAL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL,
	name VARCHAR(23) NOT NULL,
	owner_id BIGINT NOT NULL REFERENCES mes.profiles(id)
);
