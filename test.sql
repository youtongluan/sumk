/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2016-09-08 17:28:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `demo2`
-- ----------------------------
DROP TABLE IF EXISTS `demo2`;
CREATE TABLE `demo2` (
  `id` decimal(12,0) NOT NULL DEFAULT '0',
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of demo2
-- ----------------------------

-- ----------------------------
-- Table structure for `demouser`
-- ----------------------------
DROP TABLE IF EXISTS `demouser`;
CREATE TABLE `demouser` (
  `id` decimal(20,0) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `lastUpate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demouser
-- ----------------------------

-- ----------------------------
-- Table structure for `multikey`
-- ----------------------------
DROP TABLE IF EXISTS `multikey`;
CREATE TABLE `multikey` (
  `id1` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `id2` varchar(100) NOT NULL DEFAULT '',
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id1`,`id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of multikey
-- ----------------------------

