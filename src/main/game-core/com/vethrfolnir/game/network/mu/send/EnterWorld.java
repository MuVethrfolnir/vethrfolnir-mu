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

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class EnterWorld extends WritePacket {

	@FetchIndex
	private ComponentIndex<PlayerState> state;
	
	@FetchIndex
	private ComponentIndex<PlayerStats> stats;
	
	@FetchIndex
	private ComponentIndex<Positioning> positioning;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = as(context);
		GameObject entity = client.getEntity();

		PlayerState state = entity.get(this.state);
		PlayerStats stats = entity.get(this.stats);
		Positioning positioning = entity.get(this.positioning);
		
		writeArray(buff, 0xC3, 0x00, 0xF3, 0x03);
		writeC(buff, positioning.getX());
		writeC(buff, positioning.getY());
		writeC(buff, positioning.getMapId());
		writeC(buff, positioning.getHeading());

		writeD(buff, (int) stats.getCurrentExperience()); // current xp
		writeD(buff, (int) stats.getNextExperience()); // needed xp
		
		writeD(buff, (int) stats.getCurrentExperience()); // current xp
		writeD(buff, (int) stats.getNextExperience()); // needed xp
		
		writeSh(buff, stats.getFreePoints());
		writeSh(buff, stats.getStrength());
		writeSh(buff, stats.getAgility());
		writeSh(buff, stats.getVitality());
		writeSh(buff, stats.getEnergy());
		
		writeSh(buff, stats.getHealthPoints());
		writeSh(buff, stats.getMaxHealthPoints());
		writeSh(buff, stats.getManaPoints());
		writeSh(buff, stats.getMaxManaPoints());
		writeSh(buff, stats.getCombatPoints());
		writeSh(buff, stats.getMaxCombatPoints());
		writeSh(buff, stats.getStaminaPoints());
		writeSh(buff, stats.getMaxStaminaPoints());
		
		writeSh(buff, 0x00); //??
		writeD(buff, state.getZen()); // money
		
		writeC(buff, 0x03); // pk level
		writeC(buff, state.getAccessLevel() > 0 ? 0x20 : 0x00); // ctl code
		
		writeSh(buff, 0x00); // fruits used
		writeSh(buff, 0x00); // fruits that can be used
		
		writeSh(buff, stats.getCommand());
		
		writeSh(buff, 0x20); // appears next to fruits stats ...
		writeSh(buff, 0x02); // appears next to fruits stats ...
		writeSh(buff, state.getInventoryExpanded());
	}

}
