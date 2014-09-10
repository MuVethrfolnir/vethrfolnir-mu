/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class DeleteObject extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		GameObject entity = as(params[0]);
		
		writeArray(buff, 0xC1, 0x06, 0x14);
		writeC(buff, 0x01); // Count of ids to forget
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
	}

}
