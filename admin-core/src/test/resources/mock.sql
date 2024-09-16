DROP TABLE IF EXISTS `mock`;
CREATE TABLE `mock`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime     NOT NULL COMMENT 'create time',
    `update_time` datetime     NOT NULL COMMENT 'update time',
    `create_user` bigint       NOT NULL COMMENT 'create user',
    `update_user` bigint       NOT NULL COMMENT 'update user',
    `name`        varchar(100) NOT NULL COMMENT 'name',
    `read_only`   varchar(100) NULL COMMENT 'read_only',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='Mock';

INSERT INTO `mock`
    (id, name, create_time, update_time, create_user, update_user)
VALUES (1, 'mock1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
       (2, 'mock2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0)
;