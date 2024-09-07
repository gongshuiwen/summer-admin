-- ----------------------------
-- Menu Data
-- ----------------------------
INSERT INTO `menu`
(id, create_time, update_time, parent_id, parent_path, name, order_num, title, icon, path, redirect, component)
VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '', 'system', 10, '系统管理', 'ep:menu', '/system', 'system/department', ''),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, '1', 'department', 1, '部门管理', '', '/system/department', '', 'DepartmentView'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, '1', 'user', 2, '用户管理', 'ep:user-filled', '/system/user', '', 'UserView'),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, '1', 'role', 3, '角色管理', 'ep:avatar', '/system/role', '', 'RoleView'),
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, '1', 'menu', 4, '菜单管理', '', '/system/menu', '', 'MenuView');
