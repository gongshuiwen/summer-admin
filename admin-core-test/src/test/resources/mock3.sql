DROP TABLE IF EXISTS `mock3`;
CREATE TABLE `mock3`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime     NOT NULL COMMENT 'create time',
    `update_time` datetime     NOT NULL COMMENT 'update time',
    `create_user` bigint       NOT NULL COMMENT 'create user',
    `update_user` bigint       NOT NULL COMMENT 'update user',
    `name`        varchar(100) NOT NULL COMMENT 'name',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='Mock 3';

INSERT INTO `mock3`
    (id, name, create_time, update_time, create_user, update_user)
VALUES (1, 'mock1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
       (2, 'mock2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0)
;