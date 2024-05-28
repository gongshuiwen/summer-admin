-- ----------------------------
-- Permission Data
-- ----------------------------
INSERT INTO `permission`
(id, code, name)
VALUES
(1, 'User:SELECT', '用户查询'),
(2, 'User:CREATE', '用户创建'),
(3, 'User:UPDATE', '用户更新'),
(4, 'User:DELETE', '用户删除'),
(5, 'Menu:SELECT', '菜单查询'),
(6, 'Menu:CREATE', '菜单创建'),
(7, 'Menu:UPDATE', '菜单更新'),
(8, 'Menu:DELETE', '菜单删除'),
(9, 'Department:SELECT', '部门查询'),
(10, 'Department:CREATE', '部门创建'),
(11, 'Department:UPDATE', '部门更新'),
(12, 'Department:DELETE', '部门删除'),
(13, 'Role:SELECT', '角色查询'),
(14, 'Role:CREATE', '角色创建'),
(15, 'Role:UPDATE', '角色更新'),
(16, 'Role:DELETE', '角色删除')
;