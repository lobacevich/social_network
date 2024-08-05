INSERT INTO mes.requests_friendship (status, update_date, recipient_id, sender_id) VALUES
('APPROVED', NOW(), 1, 2),
('APPROVED', NOW(), 3, 1),
('APPROVED', NOW(), 4, 3),
('APPROVED', NOW(), 5, 2),
('APPROVED', NOW(), 2, 5),
('APPROVED', NOW(), 4, 5),
('SENT', NOW(), 1, 3),
('SENT', NOW(), 2, 4),
('SENT', NOW(), 3, 5),
('SENT', NOW(), 1, 4);