/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class MoveObject extends MuWritePacket {

	@FetchIndex
	private ComponentIndex<Positioning> pos;

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		GameObject entity = as(params[0]);
		Positioning positioning = entity.get(pos);
		
		writeArray(buff, 0xC1, 0x08, 0xD4);
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
		writeC(buff, positioning.getX());
		writeC(buff, positioning.getY());
		writeSh(buff, positioning.getHeading() << 4);
	}

}
