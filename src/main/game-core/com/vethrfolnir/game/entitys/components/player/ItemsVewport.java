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

import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectIntProcedure;

import java.util.ArrayList;
import java.util.BitSet;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.module.item.GroundItem;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.game.util.MuUtils;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Updatable;

/**
 * @author Vlad
 *
 */
public class ItemsVewport implements Component, Updatable {

	private final TObjectIntHashMap<GroundItem> knownIds = new TObjectIntHashMap<>();
	private final BitSet ids = new BitSet();
	private int lastFreed;
	
	private GameObject entity;
	private Positioning position; 
	
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
		position = entity.get(CreatureMapping.Positioning);
	}

	@Override
	public void update(int tick, float deltaTime) {

		if(this.entity.isVoid() || (tick != 8))
			return;
		
		ArrayList<GroundItem> items = position.getCurrentRegion().groundItems;
		for (int i = 0; i < items.size(); i++) {
			GroundItem gi = items.get(i);
			MuItem item = gi.item;
			
			boolean visibile = (int) MuUtils.distanceSquared(position.getX(), position.getY(), gi.x, gi.y) <= 12;
			boolean contains = knownIds.contains(gi);
			
			if(visibile && !contains) {
				int indexId = ids.nextClearBit(lastFreed);
				ids.set(indexId);
				
				knownIds.put(gi, indexId);
				this.entity.sendPacket(MuPackets.ShowGroundItem, item, indexId, gi.isDrop); // meh for now

				System.out.println("Adding: "+item+" to "+this.entity);
				continue;
			}
			
			if(gi.isVoid || (!visibile && contains)) {
				System.out.println("Removing: "+item+" from "+this.entity);

				lastFreed = knownIds.remove(gi);
				ids.clear(lastFreed);
				this.entity.sendPacket(MuPackets.DeleteGroundItem, lastFreed);
			}
		}
	}
	
	/**
	 * @param objId
	 * @return
	 */
	public void pickUp(int objId) {
		knownIds.forEachEntry(new TObjectIntProcedure<GroundItem>() {
			@Override
			public boolean execute(GroundItem gi, int b) {
				if(objId == b) {
					Region region = position.getCurrentRegion();
					
					if(gi == null) {
						MuLogger.e(entity+" tried to pickup an unexisting item!");
						return false;
					}
					
					Inventory inv = entity.get(CreatureMapping.Inventory);
					
					if(inv.store(gi.item)) {
						gi.setVoid();
						entity.sendPacket(MuPackets.DeleteGroundItem, objId);
						lastFreed = knownIds.remove(gi);
						region.needsCleanup();
					}
					
					return false;
				}
				return true;
			}
		});
	}
	
	public void removeGroundItem(MuItem item) {
		for (int i = 0; i < knownIds.size(); i++) {
			if(knownIds.get(i) == item.getObjectId()) {
				this.entity.sendPacket(MuPackets.DeleteGroundItem, i);
				knownIds.remove(i);
				break;
			}
		}
	}
	
	@Override
	public void dispose() {
		knownIds.clear();
	}

}
