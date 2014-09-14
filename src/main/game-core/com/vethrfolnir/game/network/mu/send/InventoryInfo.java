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
public class InventoryInfo extends WritePacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		writeArray(buff, 0xC4, 0x00, 0x2D, 0xF3, 0x10);
		writeArray(buff, 0x03, 0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x10, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x0C, 0x14, 0x08, 0x1E, 0x00, 0x00, 0xD0, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x0D, 0x14, 0x10, 0x1E, 0x00, 0x00, 0xD0, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF); // content
	}

}
