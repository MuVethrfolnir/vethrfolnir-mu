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
package com.vethrfolnir.game.module.item;

/**
 * @author Vlad
 *
 */
public class ItemUtils {


	public static final void genItem(MuItem item) {
		
		int[] dataBuffer = item.getDataBuffer();
		
		int position = 0;
		int itemType = item.getTemplate().index;
		int itemIndex = item.getTemplate().id; // item type
		dataBuffer[position++] = (itemIndex & 0xFF); // item type ??
		
		// Calculate here thisItemsOptions 
		byte itemOpt = 0;
		itemOpt |= item.getItemLevel() * 8; // item level
		itemOpt |= item.getSkill() * 128; // item skill // boolean
		itemOpt |= item.getLuck() * 4; // item luck // boolean
		int addOpt = item.getAddOption(); // enchant opt
		itemOpt |= addOpt & 3; // item opt 
		
		dataBuffer[position++] = (itemOpt); // item options
		
		dataBuffer[position++] = (item.getDurabilityCount()); // item durability
		
		byte byte3 = 0;
		byte3 |= ((itemIndex & 0x100) >> 1);
		if (addOpt > 3) {
			byte3 |= 0x40; // Item +28 option
		}
		
		byte3 |= item.getAllExcOptions(); // item option
		dataBuffer[position++] = (byte3); 
		dataBuffer[position++] = (byte)item.getAncient(); // ancient item ??
		
		byte byte5 = 0;
		byte5 |= itemType << 4; // item index
		byte5 |= item.getOption380() >> 4;  // item 380 option
		dataBuffer[position++] = (byte5); // item index + 380 option

		int harmony = 0;
		harmony |= (item.getHarmonyType() & 0x0F) << 4;
		harmony |= item.getHarmonyEnchant() & 0x0F;
		dataBuffer[position++] = (harmony);//harmony option ??
		
		// item sockets
		dataBuffer[position++] = item.getSocketOption1();
		dataBuffer[position++] = item.getSocketOption2();
		dataBuffer[position++] = item.getSocketOption3();
		dataBuffer[position++] = item.getSocketOption4();
		dataBuffer[position++] = item.getSocketOption5();
	}
	
}
