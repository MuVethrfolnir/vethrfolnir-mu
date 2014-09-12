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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;

import com.vethrfolnir.game.staticdata.NpcData;
import com.vethrfolnir.game.staticdata.world.Region;

import corvus.corax.Corax;

/**
 * @author Vlad
 * TODO: Complete this. At the moment its just a WIP
 */
public class StaticData {
	
	private static TIntObjectHashMap<Region> regions = new TIntObjectHashMap<Region>();

	private static NpcData npcData;

	public static void loadData() {
		regions.put(0, new Region(0, "Lorencia"));
		
		npcData = process(new NpcData());
	}
	
	private static <T> T process(T obj) {
		Corax.pDep(obj);
		return obj;
	}

	/**
	 * @return the npcData
	 */
	public static NpcData getNpcData() {
		return npcData;
	}

	/**
	 * @param mapId
	 * @return
	 */
	public static Region getRegion(int mapId) {
		return regions.get(mapId);
	}
	
}
