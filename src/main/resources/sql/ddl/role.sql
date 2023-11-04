CREATE TABLE `role` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user` bigint NOT NULL DEFAULT '0' COMMENT '创建用户',
    `update_user` bigint NOT NULL DEFAULT '0' COMMENT '更新用户',
    `name` varchar(100) NOT NULL DEFAULT '' COMMENT '角色名称',
    `code` varchar(100) NOT NULL DEFAULT '' COMMENT '角色标识',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 1=正常,0=停用',
    `order_num` smallint NOT NULL DEFAULT '0' COMMENT '显示顺序',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息';