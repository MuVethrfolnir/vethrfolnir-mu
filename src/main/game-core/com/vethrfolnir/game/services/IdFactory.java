/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.locks.ReentrantLock;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Initiate;

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

	@Initiate
	private void load() {
		DatabaseFactory factory = Corax.getInstance(DatabaseFactory.class);
		
		try (Connection con = factory.getConnection()) {
			PreparedStatement st = con.prepareStatement("select charId from characters");
			ResultSet rs = st.executeQuery();
			ArrayList<Integer> ids = new ArrayList<Integer>();

			while(rs.next())
				ids.add(rs.getInt(1));
			
			lockIds(ids);
			log.info("Locked "+ids.size()+" id(s). Limit "+Integer.MAX_VALUE);
			ids.clear();
		}
		catch (Exception e) {
			log.fatal("Failed reading database!", e);
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
