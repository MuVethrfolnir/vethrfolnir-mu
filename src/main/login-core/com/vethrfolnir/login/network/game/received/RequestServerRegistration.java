/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
