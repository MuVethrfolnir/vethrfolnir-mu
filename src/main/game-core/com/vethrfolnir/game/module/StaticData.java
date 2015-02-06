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
package com.vethrfolnir.game.module;

import java.io.InputStream;
import java.util.ArrayList;

import com.vethrfolnir.game.staticdata.*;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.game.templates.item.ItemTemplate;
import com.vethrfolnir.game.templates.npc.NpcTemplate;
import com.vethrfolnir.game.templates.npc.SpawnTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.services.assets.AssetManager;
import com.vethrfolnir.services.assets.key.FileKey;
import com.vethrfolnir.services.assets.processors.InputStreamProcessor;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.Corax;

/**
 * @author Vlad
 * TODO: Complete this. At the moment its just a WIP
 */
public class StaticData {

	private static final MuLogger log = MuLogger.getLogger(StaticData.class);

	private static NpcData npcData;
	private static SkillData skillData;
	private static RegionData regionData;
	private static ItemData itemData;
	
	public static void loadData() {
		regionData = process(new RegionData());
		npcData = process(new NpcData());
		skillData = process(new SkillData());
		itemData = process(new ItemData());
		
		// We dont need them in memory
		spawnNpcs();
	}
	
	private static void spawnNpcs() {
		try {
			AssetManager assetManager = Corax.fetch(AssetManager.class);
			DataMappingService dms = Corax.fetch(DataMappingService.class);
			
			int skipped = 0, count = 0;
			ArrayList<InputStream> streams = assetManager.loadAssets(new FileKey("system/static/spawnlists/"), InputStreamProcessor.class, false);
			
			if(streams == null) {
				log.info("Spawnlist is empty! If you expected it not to be, check your ./system/static data folder!");
				return;
			}
			
			for (int i = 0; i < streams.size(); i++) { //XXX Maybe just make a processor later, to remove all this defining
				try(InputStream is = streams.get(i)) {
					ArrayList<SpawnTemplate> templates = dms.asArrayList(is, SpawnTemplate.class);
					
					for (int j = 0; j < templates.size(); j++) {
						SpawnTemplate template = templates.get(j);
						Region region = getRegion(template.MapId);

						if(region == null) {
							skipped++;
							skipped += template.Count;
							continue;
						}
						
						NpcTemplate npcTempalte = npcData.getTemplate(template.NpcId);
						
						if(npcTempalte == null) {
							log.warn("Cannot find npc[id="+template.NpcId+"]! Spawn skipped!");
							continue;
						}

						region.spawn(template, npcTempalte);

						count++;
						count += template.Count;
					}
					
					Disposable.dispose(templates);
				}
			}
			
			log.info("Spawned "+count+" npc(s) and skipped "+skipped);
			Disposable.dispose(streams);
		}
		catch (Exception e) {
			log.fatal("Failed parsing spawnlists!", e);
		}
	}

	private static <T> T process(T obj) {
		Corax.process(obj);
		return obj;
	}

	/**
	 * @return the npcData
	 */
	public static NpcData getNpcData() {
		return npcData;
	}

	/**
	 * @return the regionData
	 */
	public static RegionData getRegionData() {
		return regionData;
	}

	/**
	 * @return the itemData
	 */
	public static ItemData getItemData() {
		return itemData;
	}
	
	/**
	 * @param mapId
	 * @return
	 */
	public static Region getRegion(int mapId) {
		Region region = regionData.getRegion(mapId);
		
		if(region == null)
			log.warn("Cannot find region with id "+mapId+"!", new RuntimeException("404: Region not found"));
		
		return region;
	}

	/**
	 * @param skillid
	 * @return
	 */
	public static MuSkill getSkill(int skillid) {
		return skillData.getSkill(skillid);
	}

	/**
	 * @param uniqueId
	 * @return
	 */
	public static ItemTemplate getItemTemplate(int uniqueId) {
		return itemData.getTemplate(uniqueId);
	}
}
