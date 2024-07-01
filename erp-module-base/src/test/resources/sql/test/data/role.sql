-- ----------------------------
-- Role Data
-- ----------------------------
INSERT INTO role
(id, create_time, update_time, code, name)
VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,     'SYS_ADMIN',    '系统管理员'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,     'BASE_USER',    '基础用户'),
(101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,   'PLM_ADMIN',    '管理员'),
(102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,   'PLM_DESIGN',   '设计人员'),
(103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,   'PLM_PROCESS',  '工艺人员');