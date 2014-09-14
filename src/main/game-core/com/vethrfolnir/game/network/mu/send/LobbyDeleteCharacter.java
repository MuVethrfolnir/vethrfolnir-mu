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
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class LobbyDeleteCharacter extends WritePacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		boolean sucess = as(params[0]);
		
		writeC(buff, 0xC1); 
		writeC(buff, 0x05);
		writeC(buff, 0xF3);
		writeC(buff, 0x02);
		writeC(buff, sucess ? 0x01 : 0x02);

	}

}
