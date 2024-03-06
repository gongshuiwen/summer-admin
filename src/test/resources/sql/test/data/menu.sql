-- ----------------------------
-- Menu Data
-- ----------------------------
INSERT INTO `menu`
(id, parent_id, parent_path, name, order_num, title, path, component, query, url, icon)
VALUES
(1, 0, '', 'system', 10, '系统管理', 'system', '', '', '', 'system'),
(2, 1, '1', 'dept', 1, '部门管理', 'system/dept/index', '', '', '', 'dept'),
(3, 1, '1', 'user', 2, '用户管理', 'system/user/index', '', '', '', 'user'),
(4, 1, '1', 'role', 3, '角色管理', 'system/role/index', '', '', '', 'role'),
(5, 1, '1', 'menu', 4, '菜单管理', 'system/menu/index', '', '', '', 'menu');
