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
package com.vethrfolnir.game.entitys.components.player;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureStats;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.templates.PlayerTemplate;

/**
 * @author Vlad
 *
 */
public class PlayerStats extends CreatureStats {

	private PlayerState playerState;

	private int freePoints;
	private int strength = 1300; //TODO: Add starter points in ClassData
	private int agility = 1301;
	private int vitality = 1302;
	private int energy = 1303;
	private int stamina = 1034;
	private int command = 1305;

	private int masterPoints;
	private int masterLevel;
	
	private long currentExperience;
	
	
	// Item & Effect Stats
	private int luckRate;
	private int reflectRate;
	
	// Temp till we get sorted out
	private static final long[] xpTable = new long[401]; 
	
	static {
		int LevelOver_N = 1;
		for (int n = 1; n < xpTable.length; n++)
		{
			xpTable[n] = (((n+9)*n)*n)*10;
			if (n > 255 )
			{
				xpTable[n] += ((((LevelOver_N+9)*LevelOver_N)*LevelOver_N)*1000);
				LevelOver_N++;
			}
		}
	}

	public PlayerStats(PlayerTemplate template) {
		setLevel(template.level);
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.game.entitys.components.CreatureStats#initialize(com.vethrfolnir.game.entitys.GameObject)
	 */
	@Override
	public void initialize(GameObject entity) {
		super.initialize(entity);
		playerState = entity.get(PlayerState.class);
	}
	
	public void addExperiance(long xp)
	{
		currentExperience += xp;
		int o_level = this.level;
		int n_level = o_level;
		boolean lvlUp = false;
		
		for (int i = 0; i <= getMaxLevel(); i++) {
			if(getNextExperience(n_level) < getCurrentExperience()) {
				n_level++;
				lvlUp = true;
			}
		}
		
		if(n_level == o_level)
			return; // nothing to do anymore
		
		level = n_level;
		while(o_level < n_level)
		{
			// loop for how many reward's
			freePoints += playerState.getClassId().levelPoints;

			o_level++;
		}

		if(lvlUp)
		{
			// Update
			playerState.sendPacket(MuPackets.LevelUp);
		}
	}
	
	public void removeExperience(long value) {
		
		if ((currentExperience - value) < 0 )
			value = currentExperience-1;
		
		currentExperience -= value;
		
		byte minimumLevel = 1;
		byte level = minimumLevel;
		
		for (byte tmp = level; tmp <= getMaxLevel(); tmp++)
		{
			if (currentExperience >= getNextExperience(tmp))
				continue;
			level = --tmp;
			break;
		}
		if (level != this.level && level >= minimumLevel)
			super.setLevel((byte)(level - this.level));
	}
	
	public long getNextExperience() {
		return getNextExperience(level);
	}

	/**
	* Returns the experience that was required before for getting the specified level.
	**/
	public long getCurrentExperienceNeeded(int iLevel)
	{
		if(iLevel >= 1 && iLevel <= getMaxLevel()) {
			return xpTable[iLevel-1];
		}
		return 0;
	}

	public void setCurrentExperienceNeeded(int level) {
		currentExperience = getCurrentExperienceNeeded(level);
	}
	
	public long getCurrentExperience() {
		return currentExperience;
	}

	/**
	* Returns the experience required for the specified level.
	**/
	public long getNextExperience (int iLevel)
	{
		if(iLevel >= 0 && iLevel <= getMaxLevel()) {
			return xpTable[iLevel];
		}
		return 0xFFFFFFFF;
	}

	public int getMaxLevel() 
	{
		return xpTable.length - 1;
	}

	/**
	 * @return the playerState
	 */
	public PlayerState getPlayerState() {
		return playerState;
	}

	/**
	 * @param playerState the playerState to set
	 */
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	/**
	 * @return the freePoints
	 */
	public int getFreePoints() {
		return freePoints;
	}

	/**
	 * @param freePoints the freePoints to set
	 */
	public void setFreePoints(int freePoints) {
		this.freePoints = freePoints;
	}

	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * @param strength the strength to set
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	/**
	 * @return the agility
	 */
	public int getAgility() {
		return agility;
	}

	/**
	 * @param agility the agility to set
	 */
	public void setAgility(int agility) {
		this.agility = agility;
	}

	/**
	 * @return the vitality
	 */
	public int getVitality() {
		return vitality;
	}

	/**
	 * @param vitality the vitality to set
	 */
	public void setVitality(int vitality) {
		this.vitality = vitality;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * @param stamina the stamina to set
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	/**
	 * @return the command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * @return the masterPoints
	 */
	public int getMasterPoints() {
		return masterPoints;
	}

	/**
	 * @param masterPoints the masterPoints to set
	 */
	public void setMasterPoints(int masterPoints) {
		this.masterPoints = masterPoints;
	}

	/**
	 * @return the masterLevel
	 */
	public int getMasterLevel() {
		return masterLevel;
	}

	/**
	 * @param masterLevel the masterLevel to set
	 */
	public void setMasterLevel(int masterLevel) {
		this.masterLevel = masterLevel;
	}

	/**
	 * @return the luckRate
	 */
	public int getLuckRate() {
		return luckRate;
	}

	/**
	 * @param luckRate the luckRate to set
	 */
	public void setLuckRate(int luckRate) {
		this.luckRate = luckRate;
	}

	/**
	 * @return the reflectRate
	 */
	public int getReflectRate() {
		return reflectRate;
	}

	/**
	 * @param reflectRate the reflectRate to set
	 */
	public void setReflectRate(int reflectRate) {
		this.reflectRate = reflectRate;
	}

	/**
	 * @param currentExperience the currentExperience to set
	 */
	public void setCurrentExperience(long currentExperience) {
		this.currentExperience = currentExperience;
	}


	/* (non-Javadoc)
	 * @see com.vethrfolnir.game.entitys.components.CreatureStats#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		
		playerState = null;
	}
}
