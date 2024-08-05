CREATE TABLE mes.messages (
	id BIGSERIAL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL,
	message VARCHAR(255) NOT NULL,
	author_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	chat_id BIGINT NOT NULL REFERENCES mes.chats(id)  
);
