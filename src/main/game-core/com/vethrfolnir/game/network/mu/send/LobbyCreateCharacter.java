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
import com.vethrfolnir.game.templates.AccountCharacterInfo;

/**
 * @author Vlad
 *
 */
public class LobbyCreateCharacter extends MuWritePacket {

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
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
