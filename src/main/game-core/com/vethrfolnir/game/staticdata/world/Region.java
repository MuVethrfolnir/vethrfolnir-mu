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
import com.vethrfolnir.game.entitys.components.*;
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
	
	@Override
	public void dispose() {
		players.clear();
		nonPlayers.clear();
	}

}
