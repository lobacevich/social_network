CREATE TABLE mes.requests_friendship (
	id BIGSERIAL PRIMARY KEY,
	status VARCHAR(8) NOT NULL,
	update_date TIMESTAMP NOT NULL,
	recipient_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	sender_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	CONSTRAINT unique_sender_recipient UNIQUE (sender_id, recipient_id)
);
