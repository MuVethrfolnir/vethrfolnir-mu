/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.staticdata.world;

import gnu.trove.iterator.TIntIterator;

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.game.entitys.components.KnownCreatures;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.Corax;

/**
 * @author Vlad
 *
 */
public class Region implements Disposable {
	
	private static final MuLogger log = MuLogger.getLogger(Region.class);
	
	public final int regionId;
	public final String regionName;
	public int x, y;

	public final ArrayList<GameObject> players = new ArrayList<GameObject>();
	public final ArrayList<GameObject> nonPlayers = new ArrayList<GameObject>();
	
	private static final ComponentIndex<KnownCreatures> known = EntityWorld.getComponentIndex(KnownCreatures.class);
	
	private final EntityWorld entityWorld; 
	
	/**
	 * @param regionId
	 * @param regionName
	 */
	public Region(int regionId, String regionName) {
		this.regionId = regionId;
		this.regionName = regionName;
		
		entityWorld = Corax.getInstance(EntityWorld.class);
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
	 * @param entity
	 */
	public void enter(GameObject entity) {
		if(entity.isPlayer())
			players.add(entity);
		else
			nonPlayers.add(entity);
	}

	/**
	 * @param entity
	 */
	public void exit(GameObject entity) {
		if(!entity.isPlayer()) {
			log.info("Npcs cannot exit regions! They are static!", new RuntimeException("Illigal removal!"));
			return;
		}

		players.remove(entity);
		
		entity.get(known).forgetAll();
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
	
	@Override
	public void dispose() {
		players.clear();
		nonPlayers.clear();
	}

}
