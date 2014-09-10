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
public class SkillListInfo extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
        writeArray(buff, 0xc1, 0x1e, 0xf3, 0x11, 0x02, 0x00); 
        writeArray(buff, 0x00, 0x00, 0x11, 0x00);
        writeArray(buff, 01, 0x00, 0x2d, 0x00); 
	}

}
