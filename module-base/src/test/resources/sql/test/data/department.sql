-- ----------------------------
-- Department Data
-- ----------------------------
INSERT INTO department
(id, create_time, update_time, parent_id, parent_path, name, order_num, status)
VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  0, '',     '集团公司',   0, 0),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  1, '1',    '深圳总公司',  1, 0),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  1, '1',    '长沙分公司',  2, 0),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  2, '1/2',  '研发部门',   1, 0),
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  2, '1/2',  '市场部门',   2, 0),
(6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  2, '1/2',  '测试部门',   3, 0),
(7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  2, '1/2',  '财务部门',   4, 0),
(8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  2, '1/2',  '运维部门',   5, 0),
(9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  3, '1/3',  '市场部门',   1, 0),
(10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, '1/3',  '财务部门',   2, 0);
