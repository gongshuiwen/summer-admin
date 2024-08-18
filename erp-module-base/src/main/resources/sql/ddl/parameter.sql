DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    `create_user` bigint          NOT NULL DEFAULT 0 COMMENT '创建用户',
    `update_user` bigint          NOT NULL DEFAULT 0 COMMENT '更新用户',
    `status`      tinyint         NOT NULL DEFAULT 1 COMMENT '状态 1=正常,0=停用',
    `key`         varchar(50)     NOT NULL DEFAULT '' COMMENT '键',
    `value`       varchar(1000)   NOT NULL COMMENT '值',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统参数';
