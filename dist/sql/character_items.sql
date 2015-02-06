SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `character_items`
-- ----------------------------
DROP TABLE IF EXISTS `character_items`;
CREATE TABLE `character_items` (
  `ownerId` int(20) NOT NULL,
  `objectId` int(20) NOT NULL,
  `itemId` int(20) NOT NULL,
  `durabilityCount` int(20) NOT NULL,
  `itemLevel` int(20) NOT NULL,
  `slot` int(20) NOT NULL,
  `x` int(2) NOT NULL,
  `y` int(2) NOT NULL,
  `skill` int(20) NOT NULL,
  `luck` int(20) NOT NULL,
  `option` int(20) NOT NULL,
  `execOption1` int(20) NOT NULL,
  `execOption2` int(20) NOT NULL,
  `execOption3` int(20) NOT NULL,
  `execOption4` int(20) NOT NULL,
  `execOption5` int(20) NOT NULL,
  `execOption6` int(20) NOT NULL,
  `option380` int(20) NOT NULL,
  `harmonyType` int(20) NOT NULL,
  `harmonyEnchant` int(20) NOT NULL,
  `socket1` int(20) NOT NULL,
  `socket2` int(20) NOT NULL,
  `socket3` int(20) NOT NULL,
  `socket4` int(20) NOT NULL,
  `socket5` int(20) NOT NULL,
  PRIMARY KEY (`objectId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
