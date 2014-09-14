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
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.util.MuUtils;

/**
 * @author Vlad
 * This.. this is just wrong.
 */
public class MovedToLocation extends MuReadPacket {

	private static final short stepDirections[] = { -1, -1, 0, -1, 1, -1, 1, 0, 1, 1, 0, 1, -1, 1, -1, 0 };

	@FetchIndex
	private ComponentIndex<Positioning> pos;
	
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		GameObject entity = client.getEntity();
		Positioning positioning = entity.get(pos);

		int origianlX = readC(buff);
		int origianlY = readC(buff);

		int offset = buff.readerIndex();
		byte[] data = buff.array();
		
		int pack = data[offset];
		int stepCount = (pack & 0x0F) + 1; // ... seriously now ...
		int heading = (pack & 0xF0) >> 4;

		int x = origianlX, y = origianlY;

		int TableIndex = 0;
		for(int i = 1; i < stepCount; i++)
		{
			if((i % 2) == 1)
				TableIndex = data[ offset + ( i+1 ) / 2 ] >> 4;
			else
				TableIndex = data[ offset + ( i+1 ) / 2 ] & 0x0F;

			x += stepDirections [ TableIndex * 2 ];
			y += stepDirections [ TableIndex * 2 + 1 ];
		}

		// Cheap yet effective, for now
		int distance = (int) MuUtils.distanceSquared(positioning.getX(), positioning.getY(), x, y);
		if(distance > 10 && !positioning.moveFlag()) {
			client.close();
		}
			
		positioning.moveTo(x, y, heading);
		//System.out.println(getClass().getSimpleName()+": player "+entity.getName()+" "+x+" X "+y+" Y from[x "+origianlX+" y "+origianlY+"]  Heading: "+(heading)+". Step Count: "+stepCount);
	}
}
