DROP TABLE IF EXISTS `tree_mock`;
CREATE TABLE `tree_mock` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    `create_user` bigint NOT NULL DEFAULT 0 COMMENT 'create user',
    `update_user` bigint NOT NULL DEFAULT 0 COMMENT 'update user',
    `parent_id` bigint NOT NULL DEFAULT 0 COMMENT 'parent id',
    `parent_path` varchar(255) NOT NULL DEFAULT '' COMMENT 'parent path',
    PRIMARY KEY (`id`),
    KEY (`parent_id`),
    KEY (`parent_path`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tree Mock';
