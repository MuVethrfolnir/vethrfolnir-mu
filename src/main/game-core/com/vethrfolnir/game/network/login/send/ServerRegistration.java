/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
