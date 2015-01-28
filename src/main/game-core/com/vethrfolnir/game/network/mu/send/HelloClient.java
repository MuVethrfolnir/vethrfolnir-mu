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

import java.nio.ByteOrder;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

import corvus.corax.config.Config;

/**
 * @author Vlad
 *
 */
public class HelloClient extends MuWritePacket {

	@Config(key = "Client.Version", value = "-1")
	private int version;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		writeArray(buff, 0xC1, 0x0C, 0xF1, 0x00, 0x01);
		
		writeSh(buff, client.getClientId(), ByteOrder.BIG_ENDIAN); // Client Unique ID! 
		
		writeS(buff, String.valueOf(version)); // server version byte string lol
	}
}
