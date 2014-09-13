/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class PlayerTeleport extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		Region region = (Region) params[0];
		boolean sameMap = false;
		
		writeC(buff, 0xC3);
		writeC(buff, 0x0A); //size
		writeC(buff, 0x1C); //opcode
		writeC(buff, 0x2B); // dont know what this is
		writeC(buff, sameMap ? 0x00 : 0x01); // teleport type
		writeC(buff, 0x00); // move number .. wth is that ??
		writeC(buff, region.getRegionId()); // map number
		writeC(buff, region.getStartX()); // X
		writeC(buff, region.getStartY()); // Y
		writeSh(buff, 0x00); // heading not needed
	}

}
