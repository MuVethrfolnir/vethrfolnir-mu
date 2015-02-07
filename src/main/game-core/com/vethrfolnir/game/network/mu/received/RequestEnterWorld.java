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

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.*;
import com.vethrfolnir.game.network.mu.MuClient.ClientStatus;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.network.mu.send.*;
import com.vethrfolnir.game.templates.AccountCharacterInfo;

/**
 * @author Vlad
 *
 */
public class RequestEnterWorld extends MuReadPacket {

	@FetchIndex
	private ComponentIndex<PlayerState> state;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		String name = readS(buff, 10);

		AccountCharacterInfo info = client.getAccount().getLobbyCharacter(name);
		
		if(info == null)
			client.close();

		client.buildPlayer(info);
		client.setStatus(ClientStatus.InGame);

		PlayerState state = client.getEntity().get(this.state);
		client.sendPacket(MuPackets.EnterWorld);

		//client.sendPacket(new SkillListInfo());
		
		// Max HP / SD
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_HPSD, false);
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_HPSD, true);

		// Max MP / Stamina
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_MPST, false);
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_MPST, true);

		if(state.getAccessLevel() > 0)
			client.sendPacket(MuPackets.StateChange, StateChange.StateGM); // GM
		
		client.sendPacket(MuPackets.InventoryInfo, client.getEntity());
		client.sendPacket(MuPackets.SystemMessage, "Powerd by Project Vethrfolnir.", SystemMessage.MessageType.Admin);
	}

}
