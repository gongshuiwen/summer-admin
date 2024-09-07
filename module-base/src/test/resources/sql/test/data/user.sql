-- ----------------------------
-- User Data
-- admin password: admin123
-- demo  password: demo123
-- ----------------------------
INSERT INTO `user`
(id, create_time, update_time, username, password, status, nickname, email, phone, sex, avatar, login_ip, login_time, department_id)
VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', '$2a$10$F5CTo/aLZJbkn7xfpeUTRu3lYa2awkLR7873q8MJwkVClIxyEzqzG', 1, 'Administrator', '', '', 0, '', '', CURRENT_TIMESTAMP, 1),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'demo',  '$2a$10$7yIjCgH6PBqew1rThclMD.rW3vDPkUlCtWxWGRT4KgQOPZOuJeO2i', 1, 'Demo User',     '', '', 0, '', '', CURRENT_TIMESTAMP, 1);