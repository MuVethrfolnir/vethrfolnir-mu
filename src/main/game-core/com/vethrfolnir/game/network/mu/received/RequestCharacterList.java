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

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public final class RequestCharacterList extends MuReadPacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		
		// TODO When to allow Summoner
		boolean allow = CorvusConfig.getProperty("ForceAllowSummoner", true);

		client.sendPacket(MuPackets.AllowSummonerCreation, allow);
		client.sendPacket(MuPackets.EnterLobby);
	}

}
