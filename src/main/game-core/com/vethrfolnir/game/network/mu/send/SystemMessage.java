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
