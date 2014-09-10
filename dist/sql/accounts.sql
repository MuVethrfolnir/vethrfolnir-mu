/*
Navicat MySQL Data Transfer

Source Server         : localhost-5.5
Source Server Version : 50538
Source Host           : localhost:3306
Source Database       : vethrfolniremu

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2014-09-07 16:20:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `accountName` varchar(10) NOT NULL,
  `password` varchar(32) NOT NULL,
  `accessLevel` decimal(10,0) NOT NULL DEFAULT '0',
  `lastAccess` datetime NOT NULL,
  `creationDate` datetime NOT NULL,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY (`accountName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('dope', 'a006c769391e836a90b196a0f6b8c387', '0', '2014-09-07 00:00:00', '2014-09-07 00:00:00', '/192.168.0.1:64840');
INSERT INTO `accounts` VALUES ('psycho', 'e10adc3949ba59abbe56e057f20f883e', '0', '2014-09-07 00:00:00', '2014-09-07 00:00:00', '/5.12.152.9:36050');
