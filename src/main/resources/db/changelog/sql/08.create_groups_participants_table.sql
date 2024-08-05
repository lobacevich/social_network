CREATE TABLE mes.groups_participants (
	group_id BIGINT NOT NULL REFERENCES mes.groups(id),
	participant_id BIGINT NOT NULL REFERENCES mes.profiles(id),
	CONSTRAINT groups_participants_pkey PRIMARY KEY (group_id, participant_id)
);
