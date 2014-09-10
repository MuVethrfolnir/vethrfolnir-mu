/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class StatusInfo extends WritePacket {

	public static final int STATUS_HPSD = 0x01;
	public static final int STATUS_MPST = 0x02;

	@FetchIndex
	private ComponentIndex<PlayerStats> stats;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		int type = as(params[0]);
		boolean current = as(params[1]);
		
		PlayerStats stats = as(context, MuClient.class).getEntity().get(this.stats);
		
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
