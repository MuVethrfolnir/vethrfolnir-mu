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

import java.sql.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.locks.ReentrantLock;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;

/**
 * This class is Thread-Safe.<br>
 * This class is designed to be very strict with id usage.
 * 
 * @author Vlad
 * @author SoulTaker @ aion-emu
 */
public class IdFactory {

	private static final MuLogger log = MuLogger.getLogger(IdFactory.class);

	private final BitSet idList = new BitSet();

	/**
	 * Synchronization of bitset
	 */
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Id that will be used as minimal on next id request
	 */
	private int nextMinId = 0;

	@Inject
	private void load() {
		DatabaseFactory factory = Corax.fetch(DatabaseFactory.class);
		
		try (Connection con = factory.getConnection()) {
			ArrayList<Integer> ids = new ArrayList<Integer>();

			loadCharacterIds(con, ids);
			loadItemIds(con, ids);
			
			lockIds(ids);
			log.info("Locked "+ids.size()+" id(s). Limit "+Integer.MAX_VALUE);
			ids.clear();
		}
		catch (Exception e) {
			log.fatal("Failed reading database!", e);
		}
	}
	
	private void loadItemIds(Connection con, ArrayList<Integer> ids) throws SQLException {
		try (PreparedStatement st = con.prepareStatement("select objectId from character_items")) {
			ResultSet rs = st.executeQuery();
			while(rs.next())
				ids.add(rs.getInt(1));
		}
	}

	private void loadCharacterIds(Connection con, ArrayList<Integer> ids) throws SQLException {
		try (PreparedStatement st = con.prepareStatement("select charId from characters")) {
			ResultSet rs = st.executeQuery();
			while(rs.next())
				ids.add(rs.getInt(1));
		}
	}

	/**
	 * Returns next free id.
	 * 
	 * @return next free id
	 * @throws IDFactoryError
	 *             if there is no free id's
	 */
	public int obtain() {
		try {
			lock.lock();

			int id;
			if (nextMinId == Integer.MIN_VALUE) {
				// Error will be thrown few lines later, we have no more free
				// id's.
				// BitSet will throw IllegalArgumentException if nextMinId is
				// negative
				id = Integer.MIN_VALUE;
			}
			else {
				id = idList.nextClearBit(nextMinId);
			}

			// If BitSet reached Integer.MAX_VALUE size and returned last free
			// id before - it will return
			// Intger.MIN_VALUE as the next id, so we must catch such case and
			// throw error (no free id's left)
			if (id == Integer.MIN_VALUE) {
				log.error("All id's are used, please clear your database");
			}
			idList.set(id);

			// It ok to have Integer OverFlow here, on next ID request IDFactory
			// will throw error
			nextMinId = id + 1;
			return id;
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Locks given ids.
	 * 
	 * @param ids
	 *            ids to lock
	 * @throws IDFactoryError
	 *             if some of the id's were locked before
	 */
	public void lockIds(int... ids) {
		try {
			lock.lock();
			for (int id : ids) {
				boolean status = idList.get(id);
				if (status) {
					log.error("ID " + id + " is already taken, fatal error!!!");
				}
				idList.set(id);
			}
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Locks given ids.
	 * 
	 * @param ids
	 *            ids to lock
	 * @throws IDFactoryError
	 *             if some of the id's were locked before
	 */
	public void lockIds(Iterable<Integer> ids) {
		try {
			lock.lock();
			for (int id : ids) {
				boolean status = idList.get(id);
				if (status) {
					log.error("ID " + id + " is already taken, fatal error!!!");
				}
				idList.set(id);
			}
		}
		finally {
			lock.unlock();
		}
	}

	public void free(int id) {
		try {
			lock.lock();
			boolean status = idList.get(id);
			if (!status) {
				log.warn("ID " + id + " is not taken, can't release it.", new RuntimeException("Id not taken!"));
			}
			idList.clear(id);
			if (id < nextMinId || nextMinId == Integer.MIN_VALUE) {
				nextMinId = id;
			}
		}
		finally {
			lock.unlock();
		}
	}

	public int getUsedCount() {
		try {
			lock.lock();
			return idList.cardinality();
		}
		finally {
			lock.unlock();
		}
	}
}
