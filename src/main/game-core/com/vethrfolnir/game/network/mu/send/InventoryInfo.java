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
