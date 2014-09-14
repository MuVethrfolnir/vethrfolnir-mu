/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class PlayerSay extends MuWritePacket {

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		String actor = (String) params[0];
		String message = (String) params[1];
		boolean isPm = false;
		
		if(params.length > 2)
			isPm = (boolean) params[2];
		
		writeArray(buff, 0xC1, 0x00, isPm ? 0x02 : 0x00); // 0x02 - yellow message
		writeS(buff, actor, 10);
		writeS(buff, message);
		writeC(buff, 0x00);
	}

}
