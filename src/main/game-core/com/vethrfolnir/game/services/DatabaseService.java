/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.services;

import java.util.HashMap;

import com.vethrfolnir.game.services.dao.*;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 * Some thought needed, this is just a WIP
 */
public class DatabaseService {
	private static final MuLogger log = MuLogger.getLogger(DatabaseService.class);
	
	private final HashMap<Class<? extends DAO>, DAO> daos = new HashMap<>();
	
	@Initiate //XXX Awaiting Move -> DP
	private void laod() {
		
		addDAO(new AccountDAO());
		addDAO(new PlayerDAO());
		log.info("Loaded "+daos.size()+" dao(s)");
	}

	public void addDAO(DAO dao) {
		Corax.pDep(dao);
		daos.put(dao.getClass(), dao);
	}
	
	/**
	 * TODO: Update corvus engine, for better support
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DAO> T getDAO(Class<T> type) {
		return (T) daos.get(type);
	}
}
