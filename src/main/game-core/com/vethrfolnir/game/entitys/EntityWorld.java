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
package com.vethrfolnir.game.entitys;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Updatable;

import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public final class EntityWorld implements Updatable {
	private static final MuLogger log = MuLogger.getLogger(EntityWorld.class);

	private static final long ExpireTime = TimeUnit.HOURS.toMillis(1);

	private static final HashMap<Class<?>, ComponentIndex<?>> componentIndexes = new HashMap<Class<?>, ComponentIndex<?>>();
	protected static volatile int componentIndexSize;
	
	private final ArrayList<GameObject> clients = new ArrayList<>();
	private final ArrayList<GameObject> entitys = new ArrayList<>();
	private final ArrayList<GameObject> free = new ArrayList<>();

	private int tickAcum;
	
	@Inject
	public void initialize() {
		log.info("Initialized");
		obtain(); // Dummy, id's cant be 0
	}
	
	public GameObject obtain() {//TODO lock, on optimize
		GameObject entity = free.isEmpty() ? null : free.remove(0);

		if(entity == null)
			entity = new GameObject(this);

		entity.lastUsed = System.currentTimeMillis();
		entitys.add(entity);
		entity.index = entitys.indexOf(entity);
		return entity;
	}

	/**
	 * Obtains a client entity
	 * @param client
	 * @return
	 */
	public GameObject obtain(MuClient client) {
		GameObject entity = obtain();
		entity.setClient(client);
		return entity; 
	}

	/**
	 * @param entity
	 */
	public void free(GameObject entity) {
		free.add(entity);
		entity.freeIndex = free.indexOf(entity); // or size
		
		if(entity.getClient() != null) {
			clients.remove(entity);
		}
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.tools.Updatable#update(int, float)
	 */
	@Override
	public void update(int tick, float deltaTime) {
		for (int i = 0; i < entitys.size(); i++) {
			GameObject entity = entitys.get(i);

			if(entity.isVoid) {
				continue;
			}
				
			entity.update(tick, deltaTime);
		}
		
		//TODO Optimize method here
		tickAcum += tick;
		
		if(tickAcum >= 65535) {
			log.info("Performing pool cleaning!");
			tickAcum = 0;
			optimize();
		}
	}

	public void optimize() {
		ArrayList<GameObject> garbage = new ArrayList<GameObject>();
		for (int i = 0; i < free.size(); i++) {
			GameObject e = free.get(i);
			
			// Hasent been used for 1h
			if((e.lastUsed + ExpireTime) <= System.currentTimeMillis()) {
				garbage.add(e);
			}
		}

		log.info("Destroying "+garbage.size()+" unused entity(s).");
		for (int i = 0; i < garbage.size(); i++) {
			GameObject obj = garbage.get(i);
			free.remove(obj.freeIndex);
			entitys.remove(obj.index);
		}
	
//		for (int i = 0; i < entitys.size(); i++)
//			entitys.get(i).index = i;
		
		garbage.clear();
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized <T extends Component> ComponentIndex<T> getComponentIndex(Class<T> type) {
		ComponentIndex<?> index = componentIndexes.get(type);
		
		if(index == null) {
			index = new ComponentIndex<T>();
			componentIndexes.put(type, index);
		}
		
		return (ComponentIndex<T>) index;
	}
	
	/**
	 * @return the entitys
	 */
	public List<GameObject> getEntitys() {
		return Collections.unmodifiableList(entitys);
	}
	
	/**
	 * @return the clients
	 */
	public List<GameObject> getClients() {
		return Collections.unmodifiableList(clients);
	}

	/**
	 * @param name
	 * @param isPlayer
	 * @return
	 */
	public GameObject findClient(String name, boolean isPlayer) {
		for (int i = 0; i < clients.size(); i++) {
			GameObject entity =  clients.get(i);
			
			if(entity.getName() != null && entity.getName().equalsIgnoreCase(name)) {
				return entity;
			}
		}
		return null;
	}
}
