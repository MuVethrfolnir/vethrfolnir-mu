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
package com.vethrfolnir.game.network.login.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.Corax;

/**
 * @author Vlad
 *
 */
public class ReceivedNewId extends ReadPacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		Corax.config().addProperty("LoginServer.ServerId", buff.readInt());
	}

}
