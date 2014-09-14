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
 * TODO This packet needs more mining
 */
public class SystemMessage extends MuWritePacket {

	public enum MessageType {
		Admin,
		Normal,
		GuildNotice
	}
	
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		String message = as(params[0]);
		int type = as(params[1], MessageType.class).ordinal();
		
		// 0 = admin message, 1 = normal, 2 = Guild Notice
		writeArray(buff, 0xC1, 0x39 ,0x0D, type, 0xC8 ,0xC9 ,0xE4 ,0x0E ,0x17 ,0x65 ,0x83 ,0x7C ,0xB8); // obj id is some where here
		writeS(buff, message, 0x39);
	}

}
