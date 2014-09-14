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
package com.vethrfolnir.game.entitys.components;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.send.DeleteObject;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.game.util.MuUtils;
import com.vethrfolnir.tools.Updatable;


/**
 * @author Vlad
 *
 */
public class KnownCreatures implements Component, Updatable {

	public final TIntHashSet knownIds = new TIntHashSet();

	private GameObject entity;
	private Positioning positioning;

	private ComponentIndex<Positioning> pos;
	
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
		pos = EntityWorld.getComponentIndex(Positioning.class);
		positioning = this.entity.get(pos);
	}

	@Override
	public void update(int tick, float deltaTime) {

		if(this.entity.isVoid() || (tick % 8 == 1))
			return;
		
		Region currentRegion = positioning.getCurrentRegion();
		
		int playerCount = currentRegion.players.size();
		
		//TODO Profile point.
		ArrayList<GameObject> nonPlayers = currentRegion.nonPlayers; // Mobs are static
		GameObject[] players = currentRegion.players.toArray(new GameObject[playerCount]); //XXX Test me, might not be needed, but its safer
		
		for (int i = 0; i < players.length; i++) {
			GameObject entity = players[i];
			Positioning positioning = entity.get(pos);
			
			if(entity == this.entity || entity.isVoid())
				continue;
			
			boolean visibile = entity.isVisable() && (int) MuUtils.distanceSquared(positioning.x, positioning.y, this.positioning.x, this.positioning.y) <= 12;
			boolean contains = knownIds.contains(entity.getWorldIndex());
			
			if(visibile && !contains) {
				this.entity.sendPacket(MuPackets.PlayerInfo, false, entity);
				knownIds.add(entity.getWorldIndex());
				System.out.println("Adding: "+entity+" to "+this.entity);
				continue;
			}
			
			if(!visibile && contains) {
				System.out.println("Removing: "+entity+" to "+this.entity);
				this.entity.sendPacket(MuPackets.DeleteObject, entity);
				knownIds.remove(entity.getWorldIndex());
			}
		}
		
		for (int i = 0; i < nonPlayers.size(); i++) {
			GameObject entity = nonPlayers.get(i);
			Positioning positioning = entity.get(pos);
			
			if(entity == this.entity)
				continue;
			
			boolean visibile = entity.isVisable() && (int) MuUtils.distanceSquared(positioning.x, positioning.y, this.positioning.x, this.positioning.y) <= 12;
			boolean contains = knownIds.contains(entity.getWorldIndex());

			if(visibile && !contains) {
				this.entity.sendPacket(MuPackets.NpcInfo, entity);
				knownIds.add(entity.getWorldIndex());
				System.out.println("Adding Npc: "+entity+" to "+this.entity);
				continue;
			}
			
			if(!visibile && contains) {
				System.out.println("Removing Npc: "+entity+" to "+this.entity);
				this.entity.sendPacket(MuPackets.DeleteObject, entity);
				knownIds.remove(entity.getWorldIndex());
			}

		}
		
		//XXX Profile point end
	}

	public void forgetAll() {
		int[] arr = knownIds.toArray();

		DeleteObject packet = new DeleteObject(arr);
		entity.sendPacket(packet);

		EntityWorld world = entity.getWorld();

		ComponentIndex<KnownCreatures> index = EntityWorld.getComponentIndex(KnownCreatures.class);
		for (int i = 0; i < arr.length; i++) {
			int objectId = arr[i];

			GameObject entity = world.getEntitys().get(objectId);
			
			if(!entity.isPlayer())
				continue;
			
			entity.get(index).knownIds.remove(this.entity.getWorldIndex());
			entity.sendPacket(MuPackets.DeleteObject, this.entity);
		}
		
		knownIds.clear();
	}

	@Override
	public void dispose() {
		knownIds.clear();
		entity = null;
		positioning = null;
	}
}
