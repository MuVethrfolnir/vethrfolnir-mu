SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `characters`
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `accountName` varchar(10) NOT NULL,
  `charId` int(10) NOT NULL,
  `name` varchar(10) NOT NULL,
  `classId` int(10) NOT NULL,
  `level` int(10) NOT NULL DEFAULT '1',
  `currentExperience` bigint(99) NOT NULL DEFAULT '0',
  `slot` int(5) NOT NULL,
  `mapId` int(10) NOT NULL,
  `x` int(10) NOT NULL,
  `y` int(10) NOT NULL,
  `guild` int(10) NOT NULL DEFAULT '0',
  `guildRank` int(10) NOT NULL DEFAULT '0',
  `strength` int(30) NOT NULL DEFAULT '0',
  `agility` int(30) NOT NULL DEFAULT '0',
  `vitality` int(30) NOT NULL DEFAULT '0',
  `energy` int(30) NOT NULL DEFAULT '0',
  `command` int(30) NOT NULL DEFAULT '0',
  `freePoints` int(30) NOT NULL DEFAULT '0',
  `masterPoints` int(30) NOT NULL DEFAULT '0',
  `masterLevel` int(30) NOT NULL DEFAULT '0',
  `expandedInventory` int(30) NOT NULL DEFAULT '0',
  `credits` bigint(90) NOT NULL DEFAULT '0',
  `zen` bigint(90) NOT NULL DEFAULT '0',
  `accessLevel` int(30) NOT NULL DEFAULT '0',
  `wearSet` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`charId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
