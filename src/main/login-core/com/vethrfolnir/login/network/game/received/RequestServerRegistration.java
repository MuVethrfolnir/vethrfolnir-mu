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
package com.vethrfolnir.login.network.game.received;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.login.services.GameNameService.GameServer;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 *
 */
public class RequestServerRegistration extends ReadPacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {

		final GameNameService gns = as(params[0]);
		final ChannelHandlerContext ctx = as(params[1]);
		
		final int serverId = readD(buff);
		final String host = readS(buff);

		final int port = readD(buff);
		final int capacity = readD(buff);
		final String password = readS(buff);
		
		final boolean acceptAnyId = buff.readBoolean();

		// XXX: Java 8 lambda, make it a closure
		
		enqueue(new Runnable() {
			
			@Override
			public void run() {
				GameServer gn = gns.create(ctx, serverId, host, port);
				gn.setAcceptAnyId(acceptAnyId);
				gn.setCap(capacity);
				
				gns.registerNewServer(gn, password);
			}
		});
	}
}
