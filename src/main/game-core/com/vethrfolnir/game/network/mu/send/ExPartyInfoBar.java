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

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.module.MuParty;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Constantin
 *
 */
public class ExPartyInfoBar extends MuWritePacket {

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		
		MuParty party = (MuParty) params[0];
		int[] statsArray = new int[5];
		
		ArrayList<GameObject> list = party.getPartyMembers();
		for (int i = 0; i < list.size(); i++) {
			GameObject member = list.get(i);
			PlayerStats stats = member.get(PlayerMapping.PlayerStats);
			
			float percent = (float)stats.getHealthPoints() / stats.getMaxHealthPoints() * 10;
			statsArray[i] = Math.min((int) percent, 10);
		}
		
		writeC(buff, 0xC1);
		writeC(buff, 0x00);
		writeC(buff, 0x44);
		
		writeC(buff, party.size()); //count

		for (int i = 0; i < party.size(); i++) {
			writeC(buff, statsArray[i]); // percentage
		}
		
	}

}
