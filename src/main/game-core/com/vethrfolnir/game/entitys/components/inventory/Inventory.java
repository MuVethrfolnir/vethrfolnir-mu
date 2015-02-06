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

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.player.*;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.services.dao.InventoryDAO;
import com.vethrfolnir.tools.Disposable;

/**
 * @author Vlad
 *
 */
public class Inventory implements Component {

	private GameObject entity;

	private final ArrayList<MuItem> inventoryItems = new ArrayList<>();
	private final WindowType type;

	private boolean[][] slots; 

	private Appearance appearance;

	public Inventory(WindowType type) {
		this.type = type;
		slots = new boolean[type.getY()][type.getX()];;
	}
	
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
		if(entity.isPlayer()) {
			PlayerState state = entity.get(PlayerMapping.PlayerState);
			appearance = entity.get(PlayerMapping.Appearance);
			InventoryDAO dao = DatabaseAccess.InventoryAccess();
			
			ArrayList<MuItem> list = dao.loadInventory(state.getCharId());
			for (int i = 0; i < list.size(); i++) {
				MuItem item = list.get(i);
				if(EquipmentLocation.contains(item.getSlot())) {
					appearance.equipItem(item);
				}
				else {
					if(check(item)) {
						forcePut(item);
						inventoryItems.add(item);
					} // else warn
					else
						System.out.println("Wtf..");
				}
			}
			Disposable.dispose(list);
		}
	}

	/**
	 * @param item
	 * @param oldPos
	 * @param newPos
	 * @param newWindow
	 */
	public void move(MuItem item, int oldPos, int newPos, WindowType newWindow) {
		
		if(appearance == null)
			return;
		
		if(newPos < 0) {
			oldPos += getType().getOffset();
			newPos += getType().getOffset();
			if(appearance.getItem(newPos) == null) {
				remove(item);
				item.setSlot(newPos);
				inventoryItems.remove(item);
				appearance.equipItem(item);
				entity.sendPacket(MuPackets.ExInventoryMovedItem, item, newWindow);
			}
			else {
				System.out.println("Nop: cause "+appearance.getItem(newPos).getName());
				entity.sendPacket(MuPackets.ExInventoryMovedItem, item, newWindow);
			}
			
			return;
		}
		
		int y = getLine(newPos);
		int x = getColumn(newPos);

		if(item.isEquipped()) {
			System.out.println("Unequipping "+appearance.getItem(item.getSlot()));
			appearance.unequipItem(item);
		}
		else
			remove(item);

		if(check(item, x, y)) {
			item.setSlot(newPos);
			item.setOwnerId(entity);

			if(!inventoryItems.contains(item))
				inventoryItems.add(item);
				
			forcePut(item);
			entity.sendPacket(MuPackets.ExInventoryMovedItem, item, newWindow);
		}
		else {
			System.out.println("Nop! pos "+x+" - "+y+ " slot: "+newPos);
		}
		
		entity.get(PlayerMapping.Positioning).getCurrentRegion().broadcastToKnown(entity, MuPackets.PlayerInfo, false, entity);
	}

	private int getLine(int Position) {
		return Position / getType().getX();
	}

	private int getColumn(int Position) {
		return Position % getType().getX();
	}

	/**
	 * @param slot
	 * @return
	 */
	public MuItem getItem(int slot) {
		
		if(slot < 0 && appearance != null) {
			MuItem item = appearance.getItem(slot + getType().getOffset());

			if(item != null)
				return item;
		}
		
		for (int i = 0; i < inventoryItems.size(); i++) {
			MuItem item = inventoryItems.get(i);

			if(item.getSlot() == slot)
				return item;
		}
		
		return null;
	}

	/**
	 * @param itemSlot
	 */
	public void removeItem(int itemSlot) {
		MuItem item = getItem(itemSlot);
		remove(item);
		removeItem(item);
	}

	/**
	 * @param itemSlot
	 */
	public void removeItem(MuItem item) {
		if(!inventoryItems.remove(item) && appearance != null)
			appearance.removeItem(item.getSlot());

		entity.sendPacket(MuPackets.ExInventoryDeleteItem, item);
	}

	public boolean findCloses(MuItem itm) {
		for (int i = 0; i < slots.length; i++) {
			boolean[] rows = slots[i];
			for (int j = 0; j < rows.length; j++) {
				itm.setSlot((i % type.getY()) * type.getX() + (j % type.getX()));
				if(check(itm))
					return true;
			}
		}
		itm.setSlot(-1);
		return false;
	}

	private void forcePut(MuItem itm) {
		slots[itm.getY()][itm.getX()] = true;
		for (int i = 0; i < itm.getHeight(); i++) {
			int row = itm.getY() + i;
			slots[row][itm.getX()] = true;
			for (int j = 0; j < itm.getWidth(); j++) {
				slots[row][itm.getX() + j] = true;
			}
		}
	}
	
	public void remove(MuItem itm) {
		slots[itm.getY()][itm.getX()] = false;
		for (int i = 0; i < itm.getHeight(); i++) {
			int row = itm.getY() + i;
			slots[row][itm.getX()] = false;
			for (int j = 0; j < itm.getWidth(); j++) {
				slots[row][itm.getX() + j] = false;
			}
		}
	}

	public void store(MuItem item) {
		item.setOwnerId(entity);
		if(findCloses(item)) {
			forcePut(item);
			inventoryItems.add(item);
			entity.sendPacket(MuPackets.ExInventoryPlaceItem, item);
		}
	}
	
	private boolean check(MuItem itm) {
		return check(itm, itm.getX(), itm.getY());
	}
	
	private boolean check(MuItem itm, int x, int y) {
		
		if(y + itm.getHeight() > slots.length)
			return false;
		
		if(x + itm.getWidth() > slots[y].length)
			return false;
		
		if(!slots[y][x]) {
			for (int i = 0; i < itm.getHeight(); i++) {
				int row = y + i;
				if(!slots[row][x]) {
					for (int j = 0; j < itm.getWidth(); j++) {
						if(slots[row][x + j])
							return false;
					}
				}
				else
					return false;
			}
		}
		else
			return false;

		
		return true;
	}

	/**
	 * @return the type
	 */
	public WindowType getType() {
		return type;
	}
	
	@Override
	public void dispose() {
		inventoryItems.clear();
	}

	/**
	 * @return the inventoryItems
	 */
	public ArrayList<MuItem> getItems() {
		return inventoryItems;
	}
	
	/**
	 * @return
	 */
	public int itemSize() {
		return inventoryItems.size() + (appearance != null ? appearance.itemSize() : 0);
	}

}
