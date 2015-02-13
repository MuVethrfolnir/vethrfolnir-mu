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

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.player.*;
import com.vethrfolnir.game.module.MuParty;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Constantin
 *
 */
public class PartyInfo extends MuWritePacket {

	
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		MuParty party = (MuParty) params[0];
		boolean dismantle = (boolean) params[1];
		
		writeArray(buff, 0xC1, 0x00, 0x42);
		writeC(buff, dismantle ? 0 : party.size() > 0 ? 0x01 : 0x00);
		writeC(buff, dismantle ? 0 : party.size());
		
		if(dismantle)
			return;
		
		for (GameObject member : party.getPartyMembers()) {
			
			PlayerState state = member.get(PlayerMapping.PlayerState);
			PlayerStats stats = member.get(PlayerMapping.PlayerStats);
			Positioning pos = member.get(PlayerMapping.Positioning);
			
			writeS(buff, member.getName(), 10);
			writeC(buff, state.getPartySlot());
			
			writeC(buff, pos.getMapId());
			writeArray(buff, pos.getX(), pos.getY());
			
			writeArray(buff, 0x00, 0x00); // 2 bytes Unknown, could be old x y?
			writeD(buff, stats.getHealthPoints());
			writeD(buff, stats.getMaxHealthPoints());
		}
	}

}
