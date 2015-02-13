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

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.module.MuParty;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.network.mu.send.SystemMessage;
import com.vethrfolnir.game.network.mu.send.SystemMessage.MessageType;

import corvus.corax.inject.Inject;

/**
 * @author Constantin
 *
 */
public class RequestPartyInvite extends MuReadPacket {
	
	@Inject
	private EntityWorld entityWorld;

	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		int objId = readSh(buff, ByteOrder.BIG_ENDIAN);
		
		GameObject target = entityWorld.getEntitys().get(objId);

		if(target == null) {
			log.info("Client["+client+"] tried to party an non existent target "+objId);
			client.sendPacket(MuPackets.SystemMessage, "User not found in game.", SystemMessage.MessageType.Normal);
			return;
		}
		
		GameObject clientEntity = client.getEntity();
		PlayerState entityState = clientEntity.get(PlayerMapping.PlayerState);
		PlayerState targetState = target.get(PlayerMapping.PlayerState);
		
		if(entityState.getParty() != null && !entityState.getParty().isPartyLeader(clientEntity)) {
			clientEntity.sendPacket(MuPackets.SystemMessage, "You are already in a party.", MessageType.Normal);
			return;
		}
		
		if(targetState.getParty() != null) {
			clientEntity.sendPacket(MuPackets.SystemMessage, target.getName() + " is already in a party.", MessageType.Normal);
			return;
		}
		
		if(entityState.getParty() != null && entityState.getParty().size() >= MuParty.MaxPartyMembers) {
			clientEntity.sendPacket(MuPackets.SystemMessage, "Party is full.", MessageType.Normal);
			return;
		}

		target.sendPacket(MuPackets.ExPartyInvite, client.getEntity());
	}

}
