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

import com.vethrfolnir.game.templates.npc.NpcTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.tools.Disposable;

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
			Disposable.dispose(templates);
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
