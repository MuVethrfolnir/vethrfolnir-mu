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

import java.nio.ByteOrder;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class SkillUse extends MuWritePacket {

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		int skillId = (int) params[0];
		int casterId = (int) params[1];
		int targetObjID = (int) params[2];
		
		writeC(buff, 0xC3); 
		writeC(buff, 0x09); //size
		writeC(buff, 0x19); //opcode
		writeSh(buff, skillId, ByteOrder.BIG_ENDIAN); //skill id
		writeSh(buff, casterId, ByteOrder.BIG_ENDIAN); //?? objid ??
		writeSh(buff, targetObjID, ByteOrder.BIG_ENDIAN);
		shiftC(buff, 7);
	}

}
