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

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.module.MuParty;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.network.mu.send.SystemMessage;

import corvus.corax.inject.Inject;

/**
 * @author Constantin
 *
 */
public class RequestPartyJoinAnswer extends MuReadPacket {
	
	@Inject
	private EntityWorld entityWorld;

	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		boolean answer = readC(buff) == 1;
		int objId = readSh(buff, ByteOrder.BIG_ENDIAN);
		
		GameObject target = entityWorld.getEntitys().get(objId);
		
		if(target == null) {
			log.info("Client["+client+"] tried to answer party to an non existent target "+objId);
			return;
		}
		PlayerState state = target.get(PlayerMapping.PlayerState);
		if(state.getParty() == null) {
			//Create a new party
			MuParty party = new MuParty(target);
			party.invitePartyAnswer(client.getEntity(), answer);
		}
		else {
			state.getParty().invitePartyAnswer(client.getEntity(), answer);
		}
	}

}
