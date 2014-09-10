/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.login.services.GameNameService.GameServer;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class SendServerLists extends WritePacket {
	public static final SendServerLists StaticPacket = new SendServerLists();

	/**
	 * @param context
	 * @param buff
	 * @param params - GameNameService[0] 
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		GameNameService nameService = as(params[0]);
		
		int actualSize = nameService.liveServerSize();

		int size=(7 + (actualSize * 4));
		buff.writeByte(0xC2); // opcode
		
		//buff.writeShort(size);
		buff.writeByte(0x00); // size or empty ?
		buff.writeByte(size); // size?
		
		buff.writeByte(0xF4); // code
		buff.writeByte(0x06); // code
		
		buff.writeByte(0x00); // size or empty ?
		buff.writeByte(actualSize); // actual size

		// Formula
		// writeC((1 * 20 + 1));
		// 1 = server position on list, if changed to another nr it will subserver to that server id, else disapire
		// 20 = server to mult
		
		for(HashMap<Integer, GameServer> map : nameService.getLiveServers().values())
		{
			for(GameServer server : map.values())
			{
				int onlinePlayers = server.getOnlinePlayers() / server.getCap() * 100;
				
				buff.writeShort(server.getServerId());
				buff.writeShort(onlinePlayers);
			}
		}
	}
}
