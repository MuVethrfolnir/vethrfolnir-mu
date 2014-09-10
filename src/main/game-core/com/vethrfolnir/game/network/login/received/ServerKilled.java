/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.login.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 *
 */
public class ServerKilled extends ReadPacket {

	private static final MuLogger log = MuLogger.getLogger(ServerKilled.class);
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		String message = readS(buff);
		
		log.warn("Server failed registration!");
		log.warn("Reason: " + message);
		
		
		System.exit(0xBB);
	}

}
