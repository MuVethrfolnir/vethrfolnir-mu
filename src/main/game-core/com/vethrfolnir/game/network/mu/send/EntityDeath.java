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

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class EntityDeath extends MuWritePacket {

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		GameObject entity = (GameObject) params[0];
		GameObject killer = (GameObject) params[1];
		int killType = 0x00; // unk at this point

		writeArray(buff, 0xC1, 0x00, 0x17);
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
		writeC(buff, killType);
		writeSh(buff, killer.getWorldIndex(), ByteOrder.BIG_ENDIAN);
	}

}
