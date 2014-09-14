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

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class StatusInfo extends MuWritePacket {

	public static final int STATUS_HPSD = 0x01;
	public static final int STATUS_MPST = 0x02;

	@FetchIndex
	private ComponentIndex<PlayerStats> stats;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		int type = as(params[0]);
		boolean current = as(params[1]);
		
		PlayerStats stats = client.getEntity().get(this.stats);
		
		switch (type)
		{
			case STATUS_HPSD: // hp & cp
				if(!current)
				{
					writeArray(buff, 0xC1, 0x09, 0x26, 0xFE);
					writeSh(buff, stats.getMaxHealthPoints(), ByteOrder.BIG_ENDIAN); //hp
					writeC(buff, 0x00);
					writeSh(buff, stats.getMaxCombatPoints(), ByteOrder.BIG_ENDIAN);// cp
				}
				else
				{
					writeArray(buff, 0xC1, 0x09, 0x26, 0xFF);
					writeSh(buff, stats.getHealthPoints(), ByteOrder.BIG_ENDIAN); //hp
					writeC(buff, 0x00);
					writeSh(buff, stats.getCombatPoints(), ByteOrder.BIG_ENDIAN);// cp
				}
				break;
			case STATUS_MPST: // mp & stamina
				if(!current) // C1 08 27 FE 00 E2 00 37 // mp & stamina
				{
					writeArray(buff, 0xC1, 0x08, 0x27, 0xFE);
					writeSh(buff, stats.getMaxManaPoints(), ByteOrder.BIG_ENDIAN); //hp
					writeSh(buff, stats.getMaxStaminaPoints(), ByteOrder.BIG_ENDIAN);// cp
				}
				else
				{
					writeArray(buff, 0xC1, 0x08, 0x27, 0xFF);
					writeSh(buff, stats.getManaPoints(), ByteOrder.BIG_ENDIAN); //hp
					writeSh(buff, stats.getStaminaPoints(), ByteOrder.BIG_ENDIAN);// cp
				}
				break;
		}
	}

}
