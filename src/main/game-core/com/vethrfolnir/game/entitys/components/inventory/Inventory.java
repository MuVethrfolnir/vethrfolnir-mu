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
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.services.dao.InventoryDAO;
import com.vethrfolnir.game.util.SimpleArray;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Disposable;

/**
 * @author Vlad
 *
 */
public class Inventory implements Component {

	private static final MuLogger log = MuLogger.getLogger(Inventory.class);
	
	private GameObject entity;

	private final SimpleArray<MuItem> items;
	private final WindowType type;
	
	private final int invX, invY, offset;
	private int itemSize;

	protected final int[] wearBytes;
	private boolean needsRegen = true;

	public Inventory(WindowType type) {
		this.type = type;
		invX = type.getX();
		invY = type.getY();
		offset = type.getOffset();
		items = new SimpleArray<MuItem>((invX * invY) + offset);
		wearBytes = type == WindowType.InventoryWindow ? 
				new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xF3, 0x00, 0x00, 0x00, 0xF8, 0x00, 0x00, 0xF0, 0xFF, 0xFF, 0xFF, 0x00, 0x00 } : null;
	}
	
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
		if(entity.isPlayer()) {
			PlayerState state = entity.get(PlayerMapping.PlayerState);
			InventoryDAO dao = DatabaseAccess.InventoryAccess();
			
			ArrayList<MuItem> list = dao.loadInventory(state.getCharId());
			for (int i = 0; i < list.size(); i++) {
				MuItem item = list.get(i);
				put(item, item.getSlot(), true);
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
		clear(item);
		
		if(newPos < offset) { // equipment
			
			if(newPos == Paperdoll.RightHand.ordinal() && getItem(Paperdoll.LeftHand) == null)
				newPos = Paperdoll.LeftHand.ordinal();
			
			if(items.get(newPos) == null) {
				clear(item);

				item.setSlot(newPos);
				items.set(newPos, item);
				item.setEquipped(true);
				needsRegen = true;

				entity.get(CreatureMapping.Positioning).getCurrentRegion().broadcast(MuPackets.PlayerInfo, false, entity);
			}
		}
		else {
			if(check(item, newPos))
				put(item, newPos, true);
			else
				put(item, item.getSlot(), true);
		}
		
		entity.sendPacket(MuPackets.ExInventoryMovedItem, item, newWindow);
	}

	/**
	 * Returns true if space has been found for this item 
	 * @param item
	 * @return
	 */
	public boolean store(MuItem item) {
		for (int i = offset; i < items.getCapacity(); i++) {
			if(check(item, i)) {
				put(item, i, true);
				entity.sendPacket(MuPackets.ExInventoryPlaceItem, item);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param item
	 * @param i
	 * @return 
	 */
	public boolean check(MuItem item, int slot) {
		// wtf
		if(item.getWidth() > 1 && ((slot + item.getWidth()) - offset) % invX == 1) {
			return false;
		}

		for (int i = 0; i < item.getHeight(); i++) {
			for (int j = 0; j < item.getWidth(); j++) {
				int ns = (i * invX + j) + slot;
				
				if (ns >= items.getCapacity())
					return false;
				
				MuItem slotItem = items.get(ns);
				
				if(slotItem != null && slotItem != item)
					return false;
			}
		}
		
		return true;
	}

	/**
	 * @param item
	 * @param slot
	 */
	public boolean put(MuItem item, int slot, boolean force) {
		
		if(slot < offset) { // equipment
			if(force || items.get(slot) == null) {
				item.setOwnerId(entity);
				items.set(slot, item);
				item.setEquipped(true);
				needsRegen = true;
				itemSize++;
				return true;
			}
			
			return false;
		}

		if(force || check(item, slot)) {
			item.setOwnerId(entity);
			item.setEquipped(false);
			items.set(slot, item);
			item.setSlot(slot);
			itemSize++;
			for (int i = 0; i < item.getHeight(); i++) {
				for (int j = 0; j < item.getWidth(); j++) {
					int ns = i * invX + j;
					items.set(slot+ns, item);
				}
			}
			return true;
		}
		else
			log.warn("Failed placing item["+item.getName()+"] for user["+entity.getName()+"]");
		
		return false;
	}
	
	public void clear(MuItem item) {
		int slot = item.getSlot();
		items.set(slot, null);
		
		if(slot >= offset) {
			for (int i = 0; i < item.getHeight(); i++) {
				for (int j = 0; j < item.getWidth(); j++) {
					int ns = i * invX + j;
					items.set(slot+ns, null);
				}
			}
		}
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
	 * @param slot
	 * @return
	 */
	public MuItem getItem(int slot) {
		return items.get(slot);
	}

	/**
	 * @param loc
	 * @return
	 */
	public MuItem getItem(Paperdoll loc) {
		return items.get(loc.ordinal());
	}

	/**
	 * @param itemSlot
	 */
	public void removeItem(int itemSlot) {
		MuItem item = getItem(itemSlot);
		if(item != null) {
			removeItem(item);
		}
	}

	/**
	 * @param itemSlot
	 */
	public void removeItem(MuItem item) {
		clear(item);
		entity.sendPacket(MuPackets.ExInventoryDeleteItem, item);
		itemSize--;
	}

	/**
	 * @return the type
	 */
	public WindowType getType() {
		return type;
	}
	
	@Override
	public void dispose() {
		items.clear();
	}

	/**
	 * @return the items
	 */
	public SimpleArray<MuItem> getItems() {
		return items;
	}
	
	/**
	 * @return
	 */
	public int itemSize() {
		return itemSize;
	}

}
