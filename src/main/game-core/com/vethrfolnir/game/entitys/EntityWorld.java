/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Updatable;

import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 */
public final class EntityWorld implements Updatable {
	private static final MuLogger log = MuLogger.getLogger(EntityWorld.class);

	private static final long ExpireTime = TimeUnit.HOURS.toMillis(1);

	private static final HashMap<Class<?>, ComponentIndex<?>> componentIndexes = new HashMap<Class<?>, ComponentIndex<?>>();
	protected static int componentIndexSize;
	
	private final ArrayList<GameObject> clients = new ArrayList<>();
	private final ArrayList<GameObject> entitys = new ArrayList<>();
	private final ArrayList<GameObject> free = new ArrayList<>();

	private int tickAcum;
	
	@Initiate
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
		entitys.remove(entity);
		free.add(entity);
		entity.index = free.indexOf(entity);
		
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

		for (int i = 0; i < garbage.size(); i++) {
			GameObject obj = garbage.get(i);
			free.remove(obj.index);
		}
		
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
}
