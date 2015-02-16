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
package controllers.npc

import groovy.transform.CompileStatic;

import com.vethrfolnir.game.controllers.NpcController.ActionController;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.network.mu.MuPackets;

/**
 * @author Vlad
 */
@CompileStatic
class GeneralNPCActionController implements ActionController {

	@Override
	public void action(int npcId, GameObject player, GameObject npc) {
		switch(npcId) {
			case 255:
				player.sendPacket(MuPackets.CreatureSay, npcId, "What up sexy?");
				break;
		}
	}
}
