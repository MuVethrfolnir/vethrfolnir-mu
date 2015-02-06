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
package com.vethrfolnir.game.entitys.components.player;

import java.util.HashMap;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.inventory.EquipmentLocation;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.module.item.WearSet;

/**
 * @author Vlad
 *
 */
public class Appearance implements Component {
	private final HashMap<Integer, MuItem> paperdolls = new HashMap<Integer, MuItem>();
	private final int[] wearBytes = new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xF3, 0x00, 0x00, 0x00, 0xF8, 0x00, 0x00, 0xF0, 0xFF, 0xFF, 0xFF, 0x00, 0x00 };
	private boolean needsRegen = true;

	@Override
	public void initialize(GameObject entity) {
		// empty, since initiation is done in Inventory component
	}

	public void equipItem(MuItem item) {
		
		if(item.getSlot() == EquipmentLocation.RightHand.ordinal() && getItem(EquipmentLocation.LeftHand) == null)
			item.setSlot(0);

		item.setEquipped(true);
		paperdolls.put(item.getSlot(), item);
		needsRegen = true;
		System.out.println("Equipped item at slot: "+item.getSlot());
	}
	
	public void unequipItem(MuItem item) {
		if(paperdolls.remove(item.getSlot()) != null)
			item.setEquipped(false);
		
		needsRegen = true;
	}
	
	public void unequipItem(EquipmentLocation location) {
		unequipItem(getItem(location));
	}

	public void removeItem(MuItem item) {
		if(item != null)
			paperdolls.remove(item.getSlot());
	}

	public void removeItem(int slot) {
		paperdolls.remove(slot);
	}

	/**
	 * @param location
	 * @return
	 */
	public MuItem getItem(EquipmentLocation location) {
		return paperdolls.get(location.ordinal());
	}
	
	/**
	 * @param slot
	 * @return
	 */
	public MuItem getItem(int slot) {
		return paperdolls.get(slot);
	}

	/**
	 * @return the wearBytes
	 */
	public int[] getWearBytes() {
		if(needsRegen ) {
			WearSet.generateWearItems(this);
			needsRegen = false;
		}
		
		return wearBytes;
	}
	
	/**
	 * @return the paperdolls
	 */
	public HashMap<Integer, MuItem> getPaperdolls() {
		return paperdolls;
	}

	/**
	 * @return
	 */
	public int itemSize() {
		return paperdolls.size();
	}

	@Override
	public void dispose() {
		paperdolls.clear();
	}

}

