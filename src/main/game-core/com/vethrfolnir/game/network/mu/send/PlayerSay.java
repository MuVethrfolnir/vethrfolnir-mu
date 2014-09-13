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
public class PlayerSay extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		String actor = (String) params[0];
		String message = (String) params[1];
		boolean isPm = false;
		
		if(params.length > 2)
			isPm = (boolean) params[2];
		
		writeArray(buff, 0xC1, 0x00, isPm ? 0x02 : 0x00); // 0x02 - yellow message
		writeS(buff, actor, 10);
		writeNS(buff, message);
		writeC(buff, 0x00);
	}

}
