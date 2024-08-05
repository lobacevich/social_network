CREATE TABLE mes.comments (
	id BIGSERIAL PRIMARY KEY,
	text_comment VARCHAR(255) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	author_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	post_id BIGINT NOT NULL REFERENCES mes.posts(id)
);
