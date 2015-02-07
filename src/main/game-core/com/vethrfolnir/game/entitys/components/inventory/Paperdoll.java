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
package com.vethrfolnir.game.entitys.components.inventory;

/**
 * @author Vlad
 */
public enum Paperdoll {
	LeftHand,
	RightHand,
	Helmet,
	ChestPlate,
	Pants,
	Gloves,
	Boots,
	Wings,
	Pet;


	public static boolean contains(int slot) {
		for (int i = 0; i < values().length; i++) {
			Paperdoll val = values()[i];
			if(val.ordinal() == slot)
				return true;
		}
		return false;
	}
}