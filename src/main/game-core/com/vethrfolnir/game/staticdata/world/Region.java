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

import com.fasterxml.jackson.annotation.*;
import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.game.entitys.components.*;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.templates.npc.NpcTemplate;
import com.vethrfolnir.game.templates.npc.SpawnTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.Corax;
import corvus.corax.tools.Rnd;

/**
 * @author Vlad
 *
 */
public class Region implements Disposable {
	
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
	
	private static final ComponentIndex<KnownCreatures> known = EntityWorld.getComponentIndex(KnownCreatures.class);
	
	@JsonIgnore
	private final EntityWorld entityWorld; 
	
	/**
	 * Un-serialization
	 */
	public Region() {
		if(Corax.instance() != null)
			entityWorld = Corax.getInstance(EntityWorld.class);
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
			entityWorld = Corax.getInstance(EntityWorld.class);
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
		entity.get(known).forgetAll();
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
			entity.add(new CreatureStats());
			entity.commit();
			nonPlayers.add(entity);
		}
		catch(Exception e) {
			entityWorld.free(entity);
			throw e;
		}
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
		KnownCreatures knownCreatures = broadcaster.get(Region.known);
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

}
