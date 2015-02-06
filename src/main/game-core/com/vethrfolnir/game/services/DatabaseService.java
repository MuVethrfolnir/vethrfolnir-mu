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
package com.vethrfolnir.game.services;

import java.util.HashMap;

import com.vethrfolnir.game.services.dao.*;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 * Some thought needed, this is just a WIP
 */
public class DatabaseService {
	private static final MuLogger log = MuLogger.getLogger(DatabaseService.class);
	
	private final HashMap<Class<? extends DAO>, DAO> daos = new HashMap<>();
	
	@Inject //XXX Awaiting Move -> DP
	private void laod() {
		
		addDAO(new AccountDAO());
		addDAO(new PlayerDAO());
		addDAO(new InventoryDAO());
		log.info("Loaded "+daos.size()+" dao(s)");
	}

	public void addDAO(DAO dao) {
		Corax.process(dao);
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
