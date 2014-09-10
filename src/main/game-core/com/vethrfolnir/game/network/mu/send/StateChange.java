/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class StateChange extends WritePacket {

	public static final int StateGM = 0x1c;
	
	@FetchIndex
	private ComponentIndex<PlayerState> state;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = as(context);
		int effect = as(params[0]);
		
		writeArray(buff, 0xC1, 0x18, 0x07);
		writeC(buff, 0x01); // Skill sate ?
		writeSh(buff, client.getClientId(), ByteOrder.BIG_ENDIAN);
		writeC(buff, effect); // Active Effect :)
	}

}
