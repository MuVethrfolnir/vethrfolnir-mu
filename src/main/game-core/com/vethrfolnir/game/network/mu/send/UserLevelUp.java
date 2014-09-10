/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class UserLevelUp extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		PlayerStats stats = as(params[0]);

		writeC(buff, 0xC1); 
		writeC(buff, 0x18); //(size)
		writeArray(buff, 0xF3, 0x05); //(opcode)
		writeSh(buff, stats.getLevel()); //(level [unsigned char])
		writeSh(buff, stats.getFreePoints()); //(level up points [unsigned short])
		writeSh(buff, stats.getMaxHealthPoints()); //(max hp [unsigned short])
		writeSh(buff, stats.getMaxManaPoints()); //(max mp [unsigned short])
		writeSh(buff, stats.getMaxCombatPoints()); //(max shield(sd) [unsigned short])
		writeSh(buff, stats.getMaxStaminaPoints()); //(?? max stamina ?? [unsigned short])
		/*writeSh(0); //(?? Add points ?? [short])
		writeSh(0); //(?? Max Add points ?? [short])
		writeSh(0); //(?? Minus points ?? [short])
		writeSh(0); //(?? Max Minus points ?? [short])*/
		writeD(buff, (int) stats.getNextExperience());
		writeD(buff, (int) stats.getCurrentExperience());
	}

}
