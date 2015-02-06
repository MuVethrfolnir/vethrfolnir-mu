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

import com.vethrfolnir.game.templates.item.ItemTemplate;
import com.vethrfolnir.game.templates.item.ItemType;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public class ItemData {

	private static final MuLogger log = MuLogger.getLogger(ItemData.class);
	
	private TIntObjectHashMap<ItemTemplate> templates = new TIntObjectHashMap<>();

	@Inject
	public void load(DataMappingService dms) {
		try {
			ArrayList<ItemTemplate> data =  dms.asArrayList(ItemTemplate.class, "system/static/items/items.json");
			for (int i = 0; i < data.size(); i++) {
				ItemTemplate it = data.get(i);
				
				if(templates.put(it.uniqueId, it) != null)
					log.warn("Duplicate item with id: "+it.uniqueId);
			}

			log.info("Loaded "+templates.size()+" Item(s)");
			Disposable.dispose(data);
		}
		catch (Exception e) {
			log.fatal("Failed loading skills!", e);
		}
	}

	public ItemTemplate getTemplate(ItemType type, int itemId) {
		return templates.get(type.index() * 512 + itemId);
	}

	public ItemTemplate getTemplate(int index, int itemId) {
		return templates.get(index * 512 + itemId);
	}
	
	public ItemTemplate getTemplate(int uniqueId) {
		return templates.get(uniqueId);
	}
}
