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
package com.vethrfolnir.game.module;

import java.util.HashMap;

import com.vethrfolnir.game.staticdata.world.Region;

/**
 * @author Vlad
 * TODO: Complete this. At the moment its just a WIP
 */
public class Regions {
	
	private static HashMap<Integer, Region> regions = new HashMap<Integer, Region>();

	public static void loadRegions() {
		regions.put(0, new Region(0, "Lorencia"));
	}
	
	public static Region getRegion(int id) {
		return regions.get(id);
	}
	
}
