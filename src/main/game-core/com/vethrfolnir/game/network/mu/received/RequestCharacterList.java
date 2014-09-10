/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public final class RequestCharacterList extends ReadPacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = as(context);
		
		// TODO When to allow Summoner
		boolean allow = CorvusConfig.getProperty("ForceAllowSummoner", true);

		client.sendPacket(MuPackets.AllowSummonerCreation, allow);
		client.sendPacket(MuPackets.EnterLobby);
	}

}
