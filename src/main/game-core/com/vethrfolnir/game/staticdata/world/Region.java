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
package com.vethrfolnir.game.staticdata.world;

import gnu.trove.iterator.TIntIterator;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.creature.CreatureState;
import com.vethrfolnir.game.entitys.components.creature.CreatureStats;
import com.vethrfolnir.game.entitys.components.player.KnownCreatures;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.module.item.GroundItem;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.services.GameController;
import com.vethrfolnir.game.templates.npc.NpcTemplate;
import com.vethrfolnir.game.templates.npc.SpawnTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.tools.*;

import corvus.corax.Corax;

/**
 * @author Vlad
 *
 */
public class Region implements Disposable, Updatable {
	
	@SuppressWarnings("unused")
	private static final MuLogger log = MuLogger.getLogger(Region.class);
	
	private int regionId;
	private String regionName;
	private int moveLevel = -1; // -1 = not movable

	// Spawn point.. we should make it random withing the town or a list with points.. later
	@JsonProperty("spawnX")
	private int x;
	@JsonProperty("spawnY")
	private int y;

	@JsonIgnore
	public final ArrayList<GameObject> players = new ArrayList<GameObject>();
	@JsonIgnore
	public final ArrayList<GameObject> nonPlayers = new ArrayList<GameObject>();
	
	@JsonIgnore
	public final ArrayList<GroundItem> groundItems = new ArrayList<GroundItem>(); 
	
	@JsonIgnore
	private final EntityWorld entityWorld;

	private boolean needsCleanup;

	/**
	 * Un-serialization
	 */
	public Region() {
		if(Corax.instance() != null) {
			entityWorld = Corax.fetch(EntityWorld.class);
			Corax.fetch(GameController.class).subscribe(this);
		}
		else
			entityWorld = null;
	}
	
	/**
	 * @param regionId
	 * @param regionName
	 */
	public Region(int regionId, String regionName) {
		this.regionId = regionId;
		this.regionName = regionName;
		//XXX Inside eclipse.. for parsing files. Remove after done importing region based stuff
		if(Corax.instance() != null)
			entityWorld = Corax.fetch(EntityWorld.class);
		else
			entityWorld = null;
	}

	/**
	 * @param regionId
	 * @param regionName
	 * @param geoData
	 */
	public Region(int regionId, String regionName, String geoData) { // TODO implement geodata
		this(regionId, regionName);
	}

	/**
	 * @param regionId
	 * @param regionName
	 * @param startX
	 * @param startY
	 */
	public Region(int regionId, String regionName, int startX, int startY) {
		this(regionId, regionName);
		this.x = startX;
		this.y = startY;
	}

	/**
	 * @param regionId
	 * @param regionName
	 * @param geoData
	 * @param moveLevel
	 * @param startX
	 * @param startY
	 */
	public Region(int regionId, String regionName, String geoData, int moveLevel, int startX, int startY) {
		this(regionId, regionName);
		this.x = startX;
		this.y = startY;
		this.moveLevel = moveLevel;
	}
	
	@Override
	public void update(int tick, float deltaTime) {
		if(needsCleanup && tick != 10)
			return;
		
		ArrayList<GroundItem> garbage = new ArrayList<GroundItem>();
		for (int i = 0; i < groundItems.size(); i++) {
			GroundItem gi = groundItems.get(i);
			
			if(gi.isVoid)
				garbage.add(gi);
		}
		
		groundItems.removeAll(garbage);
		needsCleanup = false;
	}
	
	/**
	 * @param entity
	 */
	public void enter(GameObject entity) {
		if(!entity.isPlayer()) { // Npc's can't enter regions silly, they belong there
			return;
		}

		players.add(entity);
	}

	/**
	 * @param entity
	 */
	public void exit(GameObject entity) {
		if(!entity.isPlayer()) {  // Npc's can't exit regions silly, they belong there
			return;
		}

		players.remove(entity);
		entity.get(PlayerMapping.KnownCreatures).forgetAll();
	}

	/**
	 * @param entity
	 */
	public void transfer(GameObject entity) {
		Positioning pos = entity.get(Positioning.class);
		pos.moveFlag = true;
		pos.updateRegion(this);

		pos.getCurrentRegion().exit(entity);
		entity.sendPacket(MuPackets.PlayerTeleport, this);
		pos.getCurrentRegion().enter(entity);
	}

	/**
	 * @param template
	 * @param npcTemplate 
	 */
	public void spawn(SpawnTemplate template, NpcTemplate npcTemplate) {
		if(template.Count > 0) {
			for (int i = 0; i < template.Count; i++) { 
				int x = Rnd.get(template.StartX, template.x);
				int y = Rnd.get(template.StartY, template.y);
				spawn(x, y, template.heading, npcTemplate);
			}
		}
		else {
			spawn(template.x, template.y, template.heading, npcTemplate);
		}
	}
	
	private void spawn(int x, int y, int heading, NpcTemplate npcTemplate) {
		GameObject entity = entityWorld.obtain();
		try {
			entity.setName(npcTemplate.Name);
			entity.add(new Positioning(x, y, heading, regionId));
			entity.add(new CreatureState(npcTemplate));
			entity.add(new CreatureStats(npcTemplate));
			entity.commit();
			nonPlayers.add(entity);
		}
		catch(Exception e) {
			entityWorld.free(entity);
			throw e;
		}
	}

	/**
	 * @param item
	 * @param y 
	 * @param x 
	 * @param drop 
	 */
	public void dropItem(MuItem item, int x, int y, boolean drop) {
		groundItems.add(new GroundItem(item, x, y, drop));
	}

	/**
	 * @param objId
	 */
	public GroundItem getGroundItem(int objId) {
		for (int i = 0; i < groundItems.size(); i++) {
			GroundItem it = groundItems.get(i);
			
			if(it.item.getObjectId() == objId)
				return it;
		}
		
		return null;
	}

	/**
	 * @param packet
	 * @param params
	 */
	public void broadcast(WritePacket packet, Object... params) {
		broadcast(null, packet, params);
	}

	/**
	 * @param broadcaster
	 * @param packet
	 * @param params
	 */
	public void broadcast(GameObject broadcaster, WritePacket packet, Object... params) {
		for (int i = 0; i < players.size(); i++) {
			GameObject entity = players.get(i);
			
			if(entity == broadcaster)
				continue;
			
			entity.getClient().sendPacket(packet, params);
		}
	}

	public void broadcastToKnown(GameObject broadcaster, WritePacket packet, Object... params) {
		KnownCreatures knownCreatures = broadcaster.get(PlayerMapping.KnownCreatures);
		TIntIterator iter = knownCreatures.knownIds.iterator();
		
		while(iter.hasNext()) {
			int id = iter.next();
			
			GameObject entity = entityWorld.getEntitys().get(id);
			entity.sendPacket(packet, params);
		}
	}
	
	/**
	 * @return the regionId
	 */
	public int getRegionId() {
		return regionId;
	}
	
	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}
	
	/**
	 * @return the moveLevel
	 */
	public int getMoveLevel() {
		return moveLevel;
	}
	
	/**
	 * @return the x
	 */
	public int getStartX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getStartY() {
		return y;
	}
	
	@Override
	public void dispose() {
		players.clear();
		nonPlayers.clear();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Region[id="+regionId+" name="+regionName+"]";
	}

	public void needsCleanup() {
		needsCleanup = true;
	}
}
