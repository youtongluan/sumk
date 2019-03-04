SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `demo_user`;
CREATE TABLE `demo_user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `last_update` timestamp NULL DEFAULT NULL,
  `enable` tinyint(4) DEFAULT '1' COMMENT '1表示有效记录，0表示记录已被删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `generate`;
CREATE TABLE `generate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `multikey`;
CREATE TABLE `multikey` (
  `id1` varchar(255) NOT NULL COMMENT '联合主键1',
  `id2` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL COMMENT '联合主键2',
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id1`,`id2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `odd`;
CREATE TABLE `odd` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` mediumblob,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2208669043496333243 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `school_fuzhou`;
CREATE TABLE `school_fuzhou` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `test_table`;
CREATE TABLE `test_table` (
  `id` bigint(20) NOT NULL COMMENT 'Long 类型,第1列',
  `userid` varchar(255) NOT NULL COMMENT 'String 类型，联合索引1,第2列',
  `enable` bit(1) NOT NULL COMMENT 'boolean 类型，联合索引2,第3列',
  `height` smallint(6) DEFAULT NULL COMMENT 'Short 类型,第4列',
  `age` tinyint(4) DEFAULT NULL COMMENT 'byte 类型,第5列',
  `f` decimal(12,3) DEFAULT NULL COMMENT 'float 类型,第6列',
  `d` decimal(20,5) DEFAULT NULL COMMENT 'double 类型,第7列',
  `valid` bit(1) DEFAULT b'1' COMMENT '1表示有效记录，0表示记录已被删除',
  PRIMARY KEY (`id`),
  KEY `testtable_index1` (`userid`,`enable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用来测试表格自动生成';


DROP TABLE IF EXISTS `timedemo`;
CREATE TABLE `timedemo` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE `user_detail` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `addr` varchar(255) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `valid` char(1) DEFAULT '1' COMMENT '1表示有效记录，0表示记录已被删除',
  PRIMARY KEY (`id`),
  KEY `user_detail_index1` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
