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
