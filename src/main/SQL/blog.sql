DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '博客id',
  `title` varchar(1024) DEFAULT NULL COMMENT '博客标题',
  `url` varchar(1024) DEFAULT NULL COMMENT '博客链接',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `read_count` int(11) DEFAULT NULL COMMENT '阅读数',
  `like_count` int(11) DEFAULT NULL COMMENT '点赞数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;