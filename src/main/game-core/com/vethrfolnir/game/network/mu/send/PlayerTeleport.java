/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
