DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
    `role_id`   bigint  NOT NULL    COMMENT '角色ID',
    `perm_id`   bigint  NOT NULL    COMMENT '权限ID',
    PRIMARY KEY (`role_id`, `perm_id`),
    KEY (`perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限信息';