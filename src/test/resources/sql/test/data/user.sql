-- ----------------------------
-- User Data
-- admin password: admin123
-- demo1 password: demo1234
-- demo2 password: demo1234
-- ----------------------------
INSERT INTO user
(id, username, password, status, nickname, email, phone, sex, avatar, login_ip, login_time, department_id)
VALUES
(1, 'admin', '$2a$10$F5CTo/aLZJbkn7xfpeUTRu3lYa2awkLR7873q8MJwkVClIxyEzqzG', 1, 'Administrator', '', '', 0, '', '', CURRENT_TIMESTAMP, 103),
(2, 'demo1', '$2a$10$7yIjCgH6PBqew1rThclMD.rW3vDPkUlCtWxWGRT4KgQOPZOuJeO2i', 1, 'Demo User1', '', '', 0, '', '', CURRENT_TIMESTAMP, 103),
(3, 'demo2', '$2a$10$7yIjCgH6PBqew1rThclMD.rW3vDPkUlCtWxWGRT4KgQOPZOuJeO2i', 1, 'Demo User2', '', '', 0, '', '', CURRENT_TIMESTAMP, 103);