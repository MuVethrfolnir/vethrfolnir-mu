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
package com.vethrfolnir.game.staticdata;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;

import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.inject.Inject;

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
			Disposable.dispose(data);
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
