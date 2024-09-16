DROP TABLE IF EXISTS `mock_relation`;
CREATE TABLE `mock_relation`
(
    mock1_id bigint NOT NULL COMMENT 'mock1 id',
    mock3_id bigint NOT NULL COMMENT 'mock3 id',
    PRIMARY KEY (`mock1_id`, `mock3_id`),
    KEY (`mock3_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='Relation Mock1 Mock3';

INSERT INTO `mock_relation`
    (mock1_id, mock3_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2)
;