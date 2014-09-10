/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class LobbyCreateCharacter extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		boolean success = as(params[0]);
		AccountCharacterInfo info = as(params[1]);
		
		writeArray(buff, 0xC1, 0x2C, 0xf3, 0x01);
		writeC(buff, success ? 0x01 : 0x00); // Succeeded
		writeS(buff, info == null ? "" : info.name, 10); // writes nick on 10 bytes
		writeC(buff, info == null ? 0x00 : info.slot); // position in charlist
		writeC(buff, info == null ? 0x00 : info.level); // level 1 [2 bytes]
		writeC(buff, info == null ? 0x00 : info.access); // ctlcode
		writeC(buff, info == null ? 0x00 : info.classId << 1); // class

		if(info != null)
			writeArray(buff, info.wearBytes);
		else
			for (int i = 0; i < 12; i++)
				writeC(buff, 0x00);

	}

}
