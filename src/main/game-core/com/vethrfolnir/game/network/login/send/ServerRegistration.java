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
package com.vethrfolnir.game.network.login.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class ServerRegistration extends WritePacket {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {

		String host = CorvusConfig.getProperty("Game.External", "0.0.0.0");

		int port = CorvusConfig.getProperty("Game.Port", -1);
		int capacity = CorvusConfig.getProperty("Game.OnlineCount", -1);

		int serverId = CorvusConfig.getProperty("LoginServer.ServerId", -1);
		String password = CorvusConfig.getProperty("LoginServer.Password", "root");
		boolean acceptAnyId = CorvusConfig.getProperty("LoginServer.AcceptAnyId", false);
		
		writeC(buff, 0x0A);
		writeD(buff, serverId);
		writeS(buff, host);
		writeD(buff, port);
		writeD(buff, capacity);
		writeS(buff, password);
		
		buff.writeBoolean(acceptAnyId);
	}
}
