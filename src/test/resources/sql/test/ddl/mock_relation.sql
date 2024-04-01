DROP TABLE IF EXISTS `mock_relation`;
CREATE TABLE `mock_relation` (
    mock1_id bigint NOT NULL COMMENT 'mock1 id',
    mock2_id bigint NOT NULL COMMENT 'mock2 id',
    PRIMARY KEY (`mock1_id`, `mock2_id`),
    KEY (`mock2_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Relation Mock1 Mock2';
