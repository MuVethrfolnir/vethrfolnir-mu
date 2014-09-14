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
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.staticdata.world.Region;

/**
 * @author Vlad
 *
 */
public class ValidateStateChange extends MuReadPacket {

	@FetchIndex
	private ComponentIndex<PlayerState> state;

	@FetchIndex
	private ComponentIndex<Positioning> positioning;

	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		int heading = readC(buff);
		int status = readC(buff); //XXX check, should we care about status replays from client? like sitting on a fence

		GameObject entity = client.getEntity();
		Positioning positioning = entity.get(this.positioning);
		positioning.updateHeading(heading);

		Region region = positioning.getCurrentRegion();

		if(region != null)
			region.broadcastToKnown(entity, MuPackets.ActionUpdate, status);
		else
			client.close();
	}

}
