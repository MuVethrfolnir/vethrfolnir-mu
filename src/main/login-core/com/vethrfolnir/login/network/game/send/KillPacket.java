/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login.network.game.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class KillPacket extends WritePacket {

	public enum KillType
	{
		INCORRECT_PASSWORD("The password is incorect."),
		INCORRECT_ID("The id is incorect."),
		ID_TAKEN("The id is already taken."), 
		NO_AVIALIABLE_ID("No availble id could be generated.");
		
		private final String _err;
		KillType(String str)
		{
			_err = str;
		}
		
		public String getErr() {
			return _err;
		}
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		writeC(buff, 0xBB);
		writeS(buff, as(params[0], KillType.class).getErr());
	}
}
