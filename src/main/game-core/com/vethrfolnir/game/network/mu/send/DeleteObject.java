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
public class DeleteObject extends MuWritePacket {

	private int[] knwonList;

	public DeleteObject() { /* Default 1 object */ }

	public DeleteObject(int[] knwonList) {
		this.knwonList = knwonList;
	}

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		if(knwonList != null) {
			writeArray(buff, 0xC1, 0x06, 0x14);
			writeC(buff, knwonList.length); // Count of ids to forget

			for (int i = 0; i < knwonList.length; i++) {
				int wi = knwonList[0];
				writeSh(buff, wi, ByteOrder.BIG_ENDIAN);
			}

			return;
		}
		
		GameObject entity = as(params[0]);
		
		writeArray(buff, 0xC1, 0x06, 0x14);
		writeC(buff, 0x01); // Count of ids to forget
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
	}

}
