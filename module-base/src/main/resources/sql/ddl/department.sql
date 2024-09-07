DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    `create_user` bigint          NOT NULL DEFAULT 0 COMMENT '创建用户',
    `update_user` bigint          NOT NULL DEFAULT 0 COMMENT '更新用户',
    `parent_id`   bigint          NOT NULL DEFAULT 0 COMMENT '父级ID',
    `parent_path` varchar(50)     NOT NULL DEFAULT '' COMMENT '父级路径',
    `status`      tinyint         NOT NULL DEFAULT 1 COMMENT '状态 1=正常,0=停用',
    `order_num`   smallint        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `name`        varchar(18)     NOT NULL DEFAULT '' COMMENT '部门名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 200
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='部门信息';
