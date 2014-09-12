/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.staticdata;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;

import com.vethrfolnir.game.templates.npc.NpcTemplate;
import com.vethrfolnir.game.util.CleanUtil;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;

import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 *
 */
public class NpcData {

	private static final MuLogger log = MuLogger.getLogger(NpcData.class);
	
	private final TIntObjectHashMap<NpcTemplate> templates = new TIntObjectHashMap<>();
	
	@Inject
	private void load(DataMappingService dms) {
		try {
			ArrayList<NpcTemplate> templates = dms.asArrayList(NpcTemplate.class, "system/static/npc/npc-data.json");
			
			for (int i = 0; i < templates.size(); i++) {
				NpcTemplate template = templates.get(i);
				this.templates.put(template.NpcId, template);
			}
			
			log.info("Loaded "+templates.size()+" Npc Template(s).");
			CleanUtil.dispose(templates);
		}
		catch (Exception e) {
			log.fatal("Failed loading npc templates!", e);
		}
	}
	
	public NpcTemplate getTemplate(int npcId) {
		return templates.get(npcId);
	}
	
	public NpcTemplate getTemplate(String name) {
		for (NpcTemplate temp : templates.valueCollection()) {
			if(temp.Name.equalsIgnoreCase(name))
				return temp;
		}
		
		return null;
	}
}
