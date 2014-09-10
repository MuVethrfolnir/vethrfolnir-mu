/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.templates;

/**
 * @author Vlad
 *
 */
public class Statistics {
	
	private int entitysPeak = 10; // Minimum 
	private int maxPlayersOnline;
	
	/**
	 * @return the entitysPeak
	 */
	public int getEntitysPeak() {
		return entitysPeak;
	}
	
	/**
	 * @param entitysPeak the entitysPeak to set
	 */
	public void setEntitysPeak(int entitysPeak) {
		this.entitysPeak = entitysPeak;
	}
	
	/**
	 * @return the maxPlayersOnline
	 */
	public int getMaxPlayersOnline() {
		return maxPlayersOnline;
	}
	
	/**
	 * @param maxPlayersOnline the maxPlayersOnline to set
	 */
	public void setMaxPlayersOnline(int maxPlayersOnline) {
		this.maxPlayersOnline = maxPlayersOnline;
	}

	
	
}
