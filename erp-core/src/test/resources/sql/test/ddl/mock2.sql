DROP TABLE IF EXISTS `mock2`;
CREATE TABLE `mock2` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    `create_user` bigint NOT NULL COMMENT 'create user',
    `update_user` bigint NOT NULL COMMENT 'update user',
    `name` varchar(100) NOT NULL COMMENT 'name',
    `mock1_id1` bigint NULL COMMENT 'mock1 id1 (restrict)',
    `mock1_id2` bigint NULL COMMENT 'mock1 id2 (cascade)',
    `mock1_id3` bigint NULL COMMENT 'mock1 id3 (set null)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Mock 2';
