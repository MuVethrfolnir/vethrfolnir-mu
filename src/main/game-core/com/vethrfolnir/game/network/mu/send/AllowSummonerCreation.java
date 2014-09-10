/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class AllowSummonerCreation extends WritePacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		boolean allow = as(params[0]);
		
		writeC(buff, 0xC1);
		writeC(buff, 0x05);
		writeC(buff, 0xDE);
		writeC(buff, 0x00);
		//writeC(buff, allow ? 0x0F : 0x00);
		buff.writeBoolean(allow);
	}

}
