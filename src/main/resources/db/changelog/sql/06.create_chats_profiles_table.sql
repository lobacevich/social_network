CREATE TABLE mes.chats_profiles (
	chat_id BIGINT NOT NULL REFERENCES mes.chats(id),
	participant_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	CONSTRAINT chat_profiles_pkey PRIMARY KEY (chat_id, participant_id)
);
