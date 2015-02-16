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
package com.vethrfolnir.game.controllers;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.vethrfolnir.game.entitys.GameObject;

/**
 * @author Vlad
 *
 */
public class NpcController {
	
	private TIntObjectHashMap<NpcController.Controller> controllers = new TIntObjectHashMap<>();
	
	@SuppressWarnings("unchecked")
	public <T extends Controller> T getController(int npcId, Class<T> type) {
		Controller obj = controllers.get(npcId);
		
		if(type.isInstance(obj))
			return (T) obj;
		
		return null;
	}
	
	public void register(NpcController.Controller con, int... npcIds) {
		for (int i = 0; i < npcIds.length; i++) {
			int npcId = npcIds[i];
			controllers.put(npcId, con);
		}
	}
	
	public void remove(int npcId) {
		controllers.remove(npcId);
	}

	public int size() {
		return controllers.size();
	}
	
	public interface Controller {
		
	}

	public interface ActionController extends Controller { // for initial
		public void action(int npcId, GameObject player, GameObject npc);
	}

}
