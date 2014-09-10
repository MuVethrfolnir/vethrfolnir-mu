/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.staticdata.world;

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.tools.Disposable;

/**
 * @author Vlad
 *
 */
public class Region implements Disposable {
	
	public final int regionId;
	public final String regionName;
	public int x, y;

	public final ArrayList<GameObject> players = new ArrayList<GameObject>();
	public final ArrayList<GameObject> nonPlayers = new ArrayList<GameObject>();
	
	/**
	 * @param regionId
	 * @param regionName
	 */
	public Region(int regionId, String regionName) {
		this.regionId = regionId;
		this.regionName = regionName;
	}

	/**
	 * @param entity
	 */
	public void enter(GameObject entity) {
		if(entity.isPlayer())
			players.add(entity);
		else
			nonPlayers.add(entity);
		
		broadcast(entity, MuPackets.PlayerInfo, false, entity);
	}

	/**
	 * @param entity
	 */
	public void exit(GameObject entity) {
		if(entity.isPlayer())
			players.remove(entity);
		else
			nonPlayers.remove(entity);
		
		//TODO Delete object
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


	/**
	 * @param regionId
	 * @param regionName
	 * @param x
	 * @param y
	 */
	public Region(int regionId, String regionName, int x, int y) {
		this.regionId = regionId;
		this.regionName = regionName;
		this.x = x;
		this.y = y;
	}

	@Override
	public void dispose() {
		players.clear();
		nonPlayers.clear();
	}

}
