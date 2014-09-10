/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class MoveObject extends WritePacket {

	@FetchIndex
	private ComponentIndex<Positioning> pos;

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		GameObject entity = as(params[0]);
		Positioning positioning = entity.get(pos);
		
		writeArray(buff, 0xC1, 0x08, 0xD4);
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
		writeC(buff, positioning.getX());
		writeC(buff, positioning.getY());
		writeSh(buff, positioning.getHeading() << 4);
	}

}
