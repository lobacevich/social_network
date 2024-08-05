CREATE INDEX idx_groups_participants_group_id 
ON mes.groups_participants(group_id);
CREATE INDEX idx_groups_participants_participant_id 
ON mes.groups_participants(participant_id);