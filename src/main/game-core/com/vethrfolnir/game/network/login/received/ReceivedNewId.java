/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.login.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.CorvusConfig;

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
		CorvusConfig.addProperty("LoginServer.ServerId", buff.readInt());
	}

}
