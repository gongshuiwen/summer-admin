DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
    `id`            bigint          NOT NULL    AUTO_INCREMENT              COMMENT 'ID',
    `create_time`   datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
    `update_time`   datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP   COMMENT '更新时间',
    `create_user`   bigint          NOT NULL    DEFAULT 0                   COMMENT '创建用户',
    `update_user`   bigint          NOT NULL    DEFAULT 0                   COMMENT '更新用户',
    `parent_id`     bigint          NOT NULL    DEFAULT 0                   COMMENT '父级ID',
    `parent_path`   varchar(50)     NOT NULL    DEFAULT ''                  COMMENT '祖级路径',
    `name`          varchar(50)     NOT NULL    DEFAULT ''                  COMMENT '菜单名称',
    `order_num`     smallint        NOT NULL    DEFAULT 0                   COMMENT '显示顺序',
    `status`        tinyint         NOT NULL    DEFAULT 0                   COMMENT '部门状态 0=正常,1=停用',
    `title`         varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '菜单标题',
    `path`          varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '路由地址',
    `component`     varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '组件路径',
    `query`         varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '路由参数',
    `url`           varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '外链地址',
    `icon`          varchar(100)    NOT NULL    DEFAULT ''                  COMMENT '菜单图标',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单信息';