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
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class StateChange extends MuWritePacket {

	public static final int StateGM = 0x1c;
	
	@FetchIndex
	private ComponentIndex<PlayerState> state;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		int effect = as(params[0]);
		
		writeArray(buff, 0xC1, 0x18, 0x07);
		writeC(buff, 0x01); // Skill sate ?
		writeSh(buff, client.getClientId(), ByteOrder.BIG_ENDIAN);
		writeC(buff, effect); // Active Effect :)
	}

}
