/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.staticdata;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;

import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.game.util.CleanUtil;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;

import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 *
 */
public class RegionData {

	private static final MuLogger log = MuLogger.getLogger(RegionData.class);

	private final TIntObjectHashMap<Region> regions = new TIntObjectHashMap<Region>();

	@Inject
	private void load(DataMappingService dms) {
		try {
			ArrayList<Region> data = dms.asArrayList(Region.class, "system/static/world-data.json");
			for (int i = 0; i < data.size(); i++) {
				Region region = data.get(i);
				regions.put(region.getRegionId(), region);
			}
			
			log.info("Loaded "+regions.size()+" region(s)");
			CleanUtil.dispose(data);
		}
		catch (Exception e) {
			log.fatal("Failed loading regions!", e);
		}
	}

	/**
	 * @param mapId
	 * @return
	 */
	public Region getRegion(int mapId) {
		return regions.get(mapId);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Region getRegion(String name) {
		Object[] values = regions.values();
		
		for (int i = 0; i < values.length; i++) {
			Region region = (Region) values[i];
			if(region.getRegionName().equalsIgnoreCase(name)) {
				return region;
			}
		}
		
		return null;
	}
	
	/**
	 * @return the regions
	 */
	public TIntObjectHashMap<Region> getRegions() {
		return regions;
	}

}
