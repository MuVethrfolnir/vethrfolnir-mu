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

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class ActionUpdate extends WritePacket {

	public static final int AttackTarget = 0x78;
	public static final int SitDown = 0x80;
	public static final int Clap = 133;
	
	public static final int SillyClap = 200;

	@FetchIndex
	private ComponentIndex<Positioning> pos;
	
	
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = (MuClient) context;
		int type = as(params[0]);
		
		GameObject target = null;

		if(params.length > 1)
			target = as(params[1]);

		Positioning positioning = client.getEntity().get(pos);
		writeArray(buff, 0xC1, 0x09, 0x18);
		writeSh(buff, client.getClientId(), ByteOrder.BIG_ENDIAN);
		writeC(buff, positioning.getHeading());
		
		writeC(buff, type); // attack type? 0x78 = melee
		
		// Target if any
		writeSh(buff, target == null ? 0x00 : target.getWorldIndex(), ByteOrder.BIG_ENDIAN);
	}

}
