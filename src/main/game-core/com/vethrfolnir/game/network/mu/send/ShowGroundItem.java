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

import com.vethrfolnir.game.module.item.GroundItem;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author PsychoJr
 */
public class ShowGroundItem extends MuWritePacket {
	
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {

		MuItem item = (MuItem) params[0];
		int groundId = (int) params[1];
		boolean isDrop = (boolean) params[2]; // visually just droped by the player

		GroundItem gi = item.getGroundItem();
		
		if(gi == null)
			return;
		
		writeArray(buff, 0xC2, 0x00, 0x15, 0x20, 0x01); // size

		if (isDrop)
			writeSh(buff, groundId + 32768, ByteOrder.BIG_ENDIAN);
		else
			writeSh(buff, groundId, ByteOrder.BIG_ENDIAN);

		writeC(buff, gi.x);
		writeC(buff, gi.y);
		writeArray(buff, item.toCode());
	}

}
