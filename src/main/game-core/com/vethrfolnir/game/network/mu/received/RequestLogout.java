/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.network.mu.*;
import com.vethrfolnir.game.network.mu.MuClient.ClientStatus;
import com.vethrfolnir.game.network.mu.send.Logout;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 *
 */
public class RequestLogout extends ReadPacket {

	@FetchIndex
	private ComponentIndex<Positioning> positioning;
	
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		int type = readC(buff);
		
		MuClient client = as(context);
		
		//TODO Checks if he can actually do it?

		Positioning positioning = client.getEntity().get(this.positioning);
		positioning.getCurrentRegion().exit(client.getEntity());
		
		switch (type) {
			case Logout.Lobby:
				client.sendPacket(MuPackets.Logout, type);
				client.setStatus(ClientStatus.Authed);
				break;
			case Logout.ServerList:
			case Logout.Exit:
				client.sendPacket(MuPackets.Logout, type);
				client.close();
				break;
		}
	}

}
