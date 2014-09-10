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
