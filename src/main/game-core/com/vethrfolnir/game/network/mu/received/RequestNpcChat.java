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
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.controllers.NpcController;
import com.vethrfolnir.game.controllers.NpcController.ActionController;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.staticdata.world.Region;

import corvus.corax.inject.Inject;

/**
 * @author PsychoJr
 */
public class RequestNpcChat extends MuReadPacket {

	@Inject
	private NpcController npcControllers; 
	
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		int npcObjId = readSh(buff);
		
		System.out.println("Hello "+npcObjId);

		Region region = client.getEntity().get(CreatureMapping.Positioning).getCurrentRegion();

		GameObject npc = region.getNpc(npcObjId);

		if(npc == null) {
			log.fatal("Invalid: "+npcObjId+" request sent by "+client);
			return;
		}
		
		int npcId = npc.get(CreatureMapping.CreatureState).getNpcId();
		
		ActionController controller = npcControllers.getController(npcId, ActionController.class);

		if(controller != null) {
			controller.action(npcId, client.getEntity(), npc);
		}
		else {
			client.sendPacket(MuPackets.CreatureSay, npc, "Action not handled yet.");
			client.sendPacket(MuPackets.PlayerSay, "NPC["+npcId+"]", "Action["+npcObjId+"] not handled yet.");
		}
	}
}
