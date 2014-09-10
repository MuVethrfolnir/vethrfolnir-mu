/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
