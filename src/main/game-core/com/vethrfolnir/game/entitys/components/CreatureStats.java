/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.entitys.components;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;

/**
 * @author Vlad
 *
 */
public class CreatureStats implements Component {

	protected GameObject entity;

	protected int maxHealthPoints = 200, maxCombatPoints = 200;
	protected int maxManaPoints = 200, maxStaminaPoints = 200;

	protected int healthPoints = 100, combatPoints = 100;
	protected int manaPoints = 100, staminaPoints = 100;

	protected int level;
	
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
	}

	/**
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the maxHealthPoints
	 */
	public int getMaxHealthPoints() {
		return maxHealthPoints;
	}

	/**
	 * @param maxHealthPoints the maxHealthPoints to set
	 */
	public void setMaxHealthPoints(int maxHealthPoints) {
		this.maxHealthPoints = maxHealthPoints;
	}

	/**
	 * @return the maxCombatPoints
	 */
	public int getMaxCombatPoints() {
		return maxCombatPoints;
	}

	/**
	 * @param maxCombatPoints the maxCombatPoints to set
	 */
	public void setMaxCombatPoints(int maxCombatPoints) {
		this.maxCombatPoints = maxCombatPoints;
	}

	/**
	 * @return the maxManaPoints
	 */
	public int getMaxManaPoints() {
		return maxManaPoints;
	}

	/**
	 * @param maxManaPoints the maxManaPoints to set
	 */
	public void setMaxManaPoints(int maxManaPoints) {
		this.maxManaPoints = maxManaPoints;
	}

	/**
	 * @return the maxStaminaPoints
	 */
	public int getMaxStaminaPoints() {
		return maxStaminaPoints;
	}

	/**
	 * @param maxStaminaPoints the maxStaminaPoints to set
	 */
	public void setMaxStaminaPoints(int maxStaminaPoints) {
		this.maxStaminaPoints = maxStaminaPoints;
	}

	/**
	 * @return the healthPoints
	 */
	public int getHealthPoints() {
		return healthPoints;
	}

	/**
	 * @param healthPoints the healthPoints to set
	 */
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	/**
	 * @return the combatPoints
	 */
	public int getCombatPoints() {
		return combatPoints;
	}

	/**
	 * @param combatPoints the combatPoints to set
	 */
	public void setCombatPoints(int combatPoints) {
		this.combatPoints = combatPoints;
	}

	/**
	 * @return the manaPoints
	 */
	public int getManaPoints() {
		return manaPoints;
	}

	/**
	 * @param manaPoints the manaPoints to set
	 */
	public void setManaPoints(int manaPoints) {
		this.manaPoints = manaPoints;
	}

	/**
	 * @return the staminaPoints
	 */
	public int getStaminaPoints() {
		return staminaPoints;
	}

	/**
	 * @param staminaPoints the staminaPoints to set
	 */
	public void setStaminaPoints(int staminaPoints) {
		this.staminaPoints = staminaPoints;
	}

	@Override
	public void dispose() {
		entity = null;
	}
}
