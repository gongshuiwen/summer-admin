DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    `create_user` bigint          NOT NULL DEFAULT 0 COMMENT '创建用户',
    `update_user` bigint          NOT NULL DEFAULT 0 COMMENT '更新用户',
    `status`      tinyint         NOT NULL DEFAULT 1 COMMENT '状态 1=正常,0=停用',
    `code`        varchar(18)     NOT NULL DEFAULT '' COMMENT '权限标识',
    `name`        varchar(18)     NOT NULL DEFAULT '' COMMENT '权限名称',
    `order_num`   smallint        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`),
    UNIQUE KEY (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限信息';