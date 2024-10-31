CREATE TABLE mes.chats ( 
	id BIGSERIAL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL,
	name VARCHAR(23),
	owner_id BIGINT NOT NULL REFERENCES mes.profiles(id)
 );
