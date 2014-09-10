/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.util.MuUtils;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 * This.. this is just wrong.
 */
public class MovedToLocation extends ReadPacket {

	private static final short stepDirections[] = { -1, -1, 0, -1, 1, -1, 1, 0, 1, 1, 0, 1, -1, 1, -1, 0 };

	@FetchIndex
	private ComponentIndex<Positioning> pos;
	
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {

		MuClient client = as(context);
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
		if(distance > 10) {
			client.close();
		}
			
		positioning.moveTo(x, y, heading);
		//System.out.println(getClass().getSimpleName()+": player "+entity.getName()+" "+x+" X "+y+" Y from[x "+origianlX+" y "+origianlY+"]  Heading: "+(heading)+". Step Count: "+stepCount);
	}
}
