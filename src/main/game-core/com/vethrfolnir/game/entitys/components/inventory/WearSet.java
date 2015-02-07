/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.entitys.components.inventory;

import com.vethrfolnir.game.module.item.MuItem;

/**
 * @author psychojr
 */
public class WearSet {

	public static final int CS_WEAPON1_TYPE = 0;
	public static final int CS_WEAPON2_TYPE = 1;

	public static final int CS_WEAPON1_DATA = 11;
	public static final int CS_WEAPON2_DATA = 12;

	public static final int CS_HELMET1 = 12;
	public static final int CS_HELMET2 = 8;
	public static final int CS_HELMET3 = 2;

	public static final int CS_ARMOR1 = 13;
	public static final int CS_ARMOR2 = 8;
	public static final int CS_ARMOR3 = 2;

	public static final int CS_PANTS1 = 13;
	public static final int CS_PANTS2 = 8;
	public static final int CS_PANTS3 = 3;

	public static final int CS_GLOVES1 = 14;
	public static final int CS_GLOVES2 = 8;
	public static final int CS_GLOVES3 = 3;

	public static final int CS_BOOTS1 = 14;
	public static final int CS_BOOTS2 = 8;
	public static final int CS_BOOTS3 = 4;

	/**
	 * Ugh..
	 * @param inventory
	 */
	public static final void generateWearItems(Inventory inventory) {
		
		int[] wearBytes = inventory.wearBytes;
		
		MuItem LeftHandWeapon = inventory.getItem(Paperdoll.LeftHand);
		MuItem RightHandWeapon = inventory.getItem(Paperdoll.RightHand);
		
		MuItem Helmet = inventory.getItem(Paperdoll.Helmet);
		MuItem Armor = inventory.getItem(Paperdoll.ChestPlate);
		MuItem Pants = inventory.getItem(Paperdoll.Pants);
		MuItem Gloves = inventory.getItem(Paperdoll.Gloves);
		MuItem Boots = inventory.getItem(Paperdoll.Boots);
		
		MuItem Wings = inventory.getItem(Paperdoll.Wings);
		
		MuItem Pet = inventory.getItem(Paperdoll.Pet);
		
		
		wearBytes[0] = LeftHandWeapon == null ? -1 : LeftHandWeapon.getItemId();
		wearBytes[11] = LeftHandWeapon == null ? 0xF0 : LeftHandWeapon.getItemIndex()*32;
		
	
		wearBytes[1] = RightHandWeapon == null ? -1 : RightHandWeapon.getItemId();
		wearBytes[12] = 0x0F;
		
		if(RightHandWeapon == null)
			wearBytes[12] = 0xF0;
		else
			wearBytes[12] = RightHandWeapon.getItemIndex()*32;
		
		//wearBytes[12] = RightHandWeapon == null ? 0x0F : RightHandWeapon.getTypeId()*32;
		
		wearBytes[CS_HELMET1] |= 0;
		wearBytes[CS_HELMET2] = 0;
		wearBytes[CS_HELMET3] = 0;
		
		wearBytes[CS_ARMOR1] = 0;
		wearBytes[CS_ARMOR2] = 0;
		wearBytes[CS_ARMOR3] = 0;
		
		wearBytes[CS_PANTS1] = 0;
		wearBytes[CS_PANTS2] = 0;
		wearBytes[CS_PANTS3] = 0;
		
		wearBytes[CS_GLOVES1] = 0;
		wearBytes[CS_GLOVES2] = 0;
		wearBytes[CS_GLOVES3] = 0;
		
		wearBytes[CS_BOOTS1] = 0;
		wearBytes[CS_BOOTS2] = 0;
		wearBytes[CS_BOOTS3] = 0;
		
		
		wearBytes[CS_HELMET1] |= CHARSET_SET_HELMET1(Helmet == null ? -1 : Helmet.getItemId());
		wearBytes[CS_HELMET2] |= CHARSET_SET_HELMET2(Helmet == null ? -1 : Helmet.getItemId());
		wearBytes[CS_HELMET3] |= CHARSET_SET_HELMET3(Helmet == null ? -1 : Helmet.getItemId());
		
		wearBytes[CS_ARMOR1] |= CHARSET_SET_ARMOR1(Armor == null ? -1 : Armor.getItemId());
		wearBytes[CS_ARMOR2] |= CHARSET_SET_ARMOR2(Armor == null ? -1 : Armor.getItemId());
		wearBytes[CS_ARMOR3] |= CHARSET_SET_ARMOR3(Armor == null ? -1 : Armor.getItemId());
		
		wearBytes[CS_PANTS1] |= CHARSET_SET_PANTS1(Pants == null ? -1 : Pants.getItemId());
		wearBytes[CS_PANTS2] |= CHARSET_SET_PANTS2(Pants == null ? -1 : Pants.getItemId());
		wearBytes[CS_PANTS3] |= CHARSET_SET_PANTS3(Pants == null ? -1 : Pants.getItemId());
		
		wearBytes[CS_GLOVES1] |= CHARSET_SET_GLOVES1(Gloves == null ? -1 : Gloves.getItemId());
		wearBytes[CS_GLOVES2] |= CHARSET_SET_GLOVES2(Gloves == null ? -1 : Gloves.getItemId());
		wearBytes[CS_GLOVES3] |= CHARSET_SET_GLOVES3(Gloves == null ? -1 : Gloves.getItemId());
		
		wearBytes[CS_BOOTS1] |= CHARSET_SET_BOOTS1(Boots == null ? -1 : Boots.getItemId());
		wearBytes[CS_BOOTS2] |= CHARSET_SET_BOOTS2(Boots == null ? -1 : Boots.getItemId());
		wearBytes[CS_BOOTS3] |= CHARSET_SET_BOOTS3(Boots == null ? -1 : Boots.getItemId());
		
		
		byte index = 0;
		
		// Set Wings
		switch (Wings == null ? -1 : Wings.getUniqueId())
		{
			case 6741: // Wings of Elf
				wearBytes[4] |= CHARSET_SET_WING1(1);
				wearBytes[8] |= 1;
				break;
			case 6145: // Wings of Heaven
				wearBytes[4] |= CHARSET_SET_WING1(1);
				wearBytes[8] |= 2;
				break;
			case 6146: // Wings of Satan
				wearBytes[4] |= CHARSET_SET_WING1(1);
				wearBytes[8] |= 3;
				break;
			case 6185: // Wings of Misery
				wearBytes[4] |= CHARSET_SET_WING1(1);
				wearBytes[8] |= 4;
				break;
			case 6147: // Muse Elf Wings
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 1;
				break;
			case 6148: // Soul Master Wings
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 2;
				break;
			case 6149: // Blade Knight Wings
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 3;
				break;
			case 6150: // Magic Gladiator Wings
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 4;
				break;
			case 6686: // Cape of Lord
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 5;
				break;
			case 6186: // Bloody Summoner Wings
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 6;
				break;
			case 6193: // Cloak of Warrior
				wearBytes[4] |= CHARSET_SET_WING1(2);
				wearBytes[8] |= 7;
				break;
			case 6180: // Blade Master Wings
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 1;
				break;
			case 6181: // Grand Master Wings
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 2;
				break;
			case 6182: // High Elf Wings
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 3;
				break;
			case 6183: // Duel Master Wings
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 4;
				break;
			case 6184: // Lord Emperor Cape
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 5;
				break;
			case 6187: // Dimension Master Wings
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 6;
				break;
			case 6194: // Fist Master Cloak
				wearBytes[4] |= CHARSET_SET_WING1(-1);
				wearBytes[8] |= 7;
				break;
			case 6274: // Small Cape of Lord
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 32;
				break;
			case 6275: // Small Wings of Misery
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 64;
				break;
			case 6276: // Small Wings of Elf
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 96;
				break;
			case 6277: // Small Wings of Heaven
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 128;
				break;
			case 6278: // Small Wings of Satan
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 160;
				break;
			case 6279: // Small Cloak of Warrior
				wearBytes[4] |= CHARSET_SET_WING1(3);
				wearBytes[16] |= 192;
				break;
			default:
				index |= 0;
		}
		
		int levelindex = 0; 
		levelindex = CHARSET_SET_SMALLLEVEL_RH(LevelSmallConvert(LeftHandWeapon == null ? 0 : LeftHandWeapon.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_LH(LevelSmallConvert(RightHandWeapon == null ? 0 : RightHandWeapon.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_HELMET(LevelSmallConvert(Helmet == null ? 0 : Helmet.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_ARMOR(LevelSmallConvert(Armor == null ? 0 : Armor.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_PANTS(LevelSmallConvert(Pants == null ? 0 : Pants.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_GLOVES(LevelSmallConvert(Gloves == null ? 0 : Gloves.getItemLevel()));
		levelindex |=  CHARSET_SET_SMALLLEVEL_BOOTS(LevelSmallConvert(Boots == null ? 0 : Boots.getItemLevel()));

		wearBytes[5] = CHARSET_SET_SMALLLEVEL1(levelindex); // item levels
		wearBytes[6] = CHARSET_SET_SMALLLEVEL2(levelindex); // item levels
		wearBytes[7] = CHARSET_SET_SMALLLEVEL3(levelindex);  // item levels
		
		wearBytes[9] = 0;
		
		if(Helmet != null && Helmet.isExcelent())
			wearBytes[9] = 0x80; // helm exc effect
		if(Armor != null && Armor.isExcelent())
			wearBytes[9] |= 0x40; // armor exc effect
		if(Pants != null && Pants.isExcelent())
			wearBytes[9] |= 0x20; // pants exc effect
		if(Gloves != null && Gloves.isExcelent())
			wearBytes[9] |= 0x10; // gloves exc effect
		if(Boots != null && Boots.isExcelent())
			wearBytes[9] |= 0x8; // boots exc effect
		if(LeftHandWeapon != null && LeftHandWeapon.isExcelent())
			wearBytes[9] |= 0x04; // left hand exc effect
		if(RightHandWeapon != null && RightHandWeapon.isExcelent())
			wearBytes[9] |= 0x02; // right hand exc effect
		
		
		wearBytes[10] = 0;

		if(Helmet != null && Helmet.isAncient())
			wearBytes[10] = 0x80;
		if(Armor != null && Armor.isAncient())
			wearBytes[10] |= 0x40;
		if(Pants != null && Pants.isAncient())
			wearBytes[10] |= 0x20;
		if(Gloves != null && Gloves.isAncient())
			wearBytes[10] |= 0x10;
		if(Boots != null && Boots.isAncient())
			wearBytes[10] |= 0x8;
		if(LeftHandWeapon != null && LeftHandWeapon.isAncient())
			wearBytes[10] |= 0x04;
		if(RightHandWeapon != null && RightHandWeapon.isAncient())
			wearBytes[10] |= 0x02;
		
		
		// Pets Set
		switch (Pet == null ? -1 : Pet.getUniqueId())
		{
			case 6656:
			case 6657:
			case 6658:
				index |= CHARSET_SET_ANGEL(Pet.getItemId());
				break;
			case 6659: // Dinorant
				index |= CHARSET_SET_ANGEL(Pet.getItemId());
				wearBytes[9] |= 0x01;
				break;
			case 6660: // Dark Horse
				index |= CHARSET_SET_ANGEL(-1);
				wearBytes[11] |= 0x01;
				break;
			case 6736: // Panda Pet
				wearBytes[15] |= 0xe0;
				break;
			case 6779: // Skeleton Pet
				//wearBytes[5] -= 3;
				wearBytes[15] |= 0x60;
				break;
			case 6723: // Rudolf Pet
				//wearBytes[5] -= 3;
				wearBytes[15] |= 0x80;
				break;
			case 6721: // Spirit of Guardian
				wearBytes[15] |= 0x40;
				break;
			case 6720: // Demon Pet
				wearBytes[15] |= 0x20;
				break;
			case 6762: // Unicorn Pet
				wearBytes[15] |= 0xa0;
				break;
			case 6693: // Fenrir
				index |= 3;
				wearBytes[9] &= 0xFE;
				wearBytes[11] &= 0xFE;
				wearBytes[11] |= 0x04;
				wearBytes[15] = 0;
	
				if (Pet.getExcelentOption1() == 1)
					wearBytes[15] |= 0x01; // Red Fenrir
	
				if (Pet.getExcelentOption2() == 1)
					wearBytes[15] |= 0x02; // Blue Fenrir
	
				if (Pet.getExcelentOption3() == 1)
					wearBytes[16] |= 0x01; // Illusion Fenrir
				break;
			default:
				index |= CHARSET_SET_ANGEL(-1);
				break;
		}
		wearBytes[4] |= index;
		
		//System.err.println("GOT WEAPON TYPE : "+wearBytes[12]);
		
		//System.err.println("User["+_actor.getName()+"] Armor ID : "+(Armor == null ? -1 : Armor.getIndexId())+" Armor is null: "+(Armor == null));
	}
	
	public static final int CHARSET_GETSMALLLEVEL(int l) {
		return l = ((l >= 13 ? 6 : (l >= 11 && l <= 12 ? 5 : (l >= 9 && l <= 10 ? 4 : (l >= 7
				&& l <= 8 ? 3 : (l >= 5 && l <= 6 ? 2 : (l >= 3 && l <= 4 ? 1 : 0)))))));
	}

	public static final int CHARSET_GET_TYPE(int x) {
		return x = (((x) & ((15) << 8)) >> 4); // 15 = MAXTYPE(16) - 1
	}

	public static final int CHARSET_GET_CLASS(int x) {
		return x = ((((x) >> 4) << 5) & (0xE0));
	}

	public static final int CHARSET_GET_CHANGEUP(int x) {
		return x = (((x) & 0x07));
	}

	public static final int CHARSET_SET_CLASS(int x) {
		return x = (((x) << 5) & 0xE0);
	}

	public static final int CHARSET_SET_CHANGEUP(int x) {
		return x = (((x) << 4) & 0x10);
	}

	public static final int CHARSET_SET_HELMET1(int x) {
		return x = (((x) & 0x1E0) >> 5);
	}

	public static final int CHARSET_SET_HELMET2(int x) {
		return x = (((x) & 0x10) << 3);
	}

	public static final int CHARSET_SET_HELMET3(int x) {
		return x = (((x) & 0x0F) << 4);
	}

	public static final int CHARSET_SET_ARMOR1(int x) {
		return x = (((x) & 0x1E0) >> 1);
	}

	public static final int CHARSET_SET_ARMOR2(int x) {
		return x = (((x) & 0x10) << 2);
	}

	public static final int CHARSET_SET_ARMOR3(int x) {
		return x = (((x) & 0x0F));
	}

	public static final int CHARSET_SET_PANTS1(int x) {
		return x = (((x) & 0x1E0) >> 5);
	}

	public static final int CHARSET_SET_PANTS2(int x) {
		return x = (((x) & 0x10) << 1);
	}

	public static final int CHARSET_SET_PANTS3(int x) {
		return x = (((x) & 0x0F) << 4);
	}

	public static final int CHARSET_SET_GLOVES1(int x) {
		return x = (((x) & 0x1E0) >> 1);
	}

	public static final int CHARSET_SET_GLOVES2(int x) {
		return x = (((x) & 0x10));
	}

	public static final int CHARSET_SET_GLOVES3(int x) {
		return x = (((x) & 0x0F));
	}

	public static final int CHARSET_SET_BOOTS1(int x) {
		return x = (((x) & 0x1E0) >> 5);
	}

	public static final int CHARSET_SET_BOOTS2(int x) {
		return x = (((x) & 0x10) >> 1);
	}

	public static final int CHARSET_SET_BOOTS3(int x) {
		return x = (((x) & 0x0F) << 4);
	}

	public static final int CHARSET_SET_WING1(int x) {
		return x = (((x) & 0x03) << 2);
	}

	public static final int CHARSET_SET_ANGEL(int x) {
		return x = (((x) & 0x03));
	}

	public static final int CHARSET_SET_SMALLLEVEL_RH(int x) {
		return x;// XXX ? = (x);
	}

	public static final int CHARSET_SET_SMALLLEVEL_LH(int x) {
		return x = ((x) << 3);
	}

	public static final int CHARSET_SET_SMALLLEVEL_HELMET(int x) {
		return x = ((x) << 6);
	}

	public static final int CHARSET_SET_SMALLLEVEL_ARMOR(int x) {
		return x = ((x) << 9);
	}

	public static final int CHARSET_SET_SMALLLEVEL_PANTS(int x) {
		return x = ((x) << 12);
	}

	public static final int CHARSET_SET_SMALLLEVEL_GLOVES(int x) {
		return x = ((x) << 15);
	}

	public static final int CHARSET_SET_SMALLLEVEL_BOOTS(int x) {
		return x = ((x) << 18);
	}

	public static final int CHARSET_SET_SMALLLEVEL1(int x) {
		return x = (((x) >> 16) & 0xFF);
	}

	public static final int CHARSET_SET_SMALLLEVEL2(int x) {
		return x = (((x) >> 8) & 0xFF);
	}

	public static final int CHARSET_SET_SMALLLEVEL3(int x) {
		return x = ((x) & 0xFF);
	}

	public static final byte LevelSmallConvert(int level) {
		// New TEST
		if (level >= 15) {
			return 7;
		}
		if (level == 14) {
			return 7;
		}
		if (level == 13) {
			return 6;
		}
		// Old
		if (level >= 13) {
			return 6;
		}

		if (level >= 11 && level <= 12) {
			return 5;
		}

		if (level >= 9 && level <= 10) {
			return 4;
		}

		if (level >= 7 && level <= 8) {
			return 3;
		}

		if (level >= 5 && level <= 6) {
			return 2;
		}

		if (level >= 3 && level <= 4) {
			return 1;
		}

		return 0;
	}

}
