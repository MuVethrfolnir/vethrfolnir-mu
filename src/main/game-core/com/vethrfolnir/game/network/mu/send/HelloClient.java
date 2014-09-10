/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.processing.annotation.Config;

/**
 * @author Vlad
 *
 */
public class HelloClient extends WritePacket {

	@Config(key = "Client.Version", value = "-1")
	private int version;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		writeArray(buff, 0xC1, 0x0C, 0xF1, 0x00, 0x01);
		
		writeSh(buff, as(context, MuClient.class).getClientId(), ByteOrder.BIG_ENDIAN); // Client Unique ID! 
		
		writeNS(buff, String.valueOf(version)); // server version byte string lol
	}
}
