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
