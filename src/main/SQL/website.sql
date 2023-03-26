DROP TABLE IF EXISTS `website`;

CREATE TABLE `website` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `site_name` varchar(255) DEFAULT NULL COMMENT '网站名称',
  `site_entry` varchar(255) DEFAULT NULL COMMENT '入口链接',
  `crawle_flag` tinyint(1) DEFAULT NULL COMMENT '是否已爬取',
  `last_crawled_time` datetime DEFAULT NULL COMMENT '上次爬取时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;