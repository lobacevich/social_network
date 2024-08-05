CREATE INDEX idx_requests_friendship_recipient_id 
ON mes.requests_friendship(recipient_id);
CREATE INDEX idx_requests_friendship_sender_id 
ON mes.requests_friendship(sender_id);
