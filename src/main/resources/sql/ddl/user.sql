DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`            bigint          NOT NULL    AUTO_INCREMENT              COMMENT 'ID',
    `create_time`   datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
    `update_time`   datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP   COMMENT '更新时间',
    `create_user`   bigint          NOT NULL    DEFAULT 0                   COMMENT '创建用户',
    `update_user`   bigint          NOT NULL    DEFAULT 0                   COMMENT '更新用户',
    `username`      varchar(100)    NOT NULL                                COMMENT '用户名',
    `password`      varchar(100)    NOT NULL                                COMMENT '密码',
    `status`        smallint        NOT NULL    DEFAULT 0                   COMMENT '状态 0=正常,1=停用',
    `nickname`      varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '昵称',
    `email`         varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '邮箱',
    `phone`         varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '手机',
    `sex`           tinyint         NOT NULL    DEFAULT 0                   COMMENT '用户性别 0=未知,1=女,2=男',
    `avatar`        varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '用户头像',
    `login_ip`      varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '最后登录IP',
    `login_time`    datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP   COMMENT '最后登录时间',
    `department_id` bigint          NOT NULL    DEFAULT 0                   COMMENT '部门ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息';