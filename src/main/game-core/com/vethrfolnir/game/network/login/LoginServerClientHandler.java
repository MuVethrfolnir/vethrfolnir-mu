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
package com.vethrfolnir.game.network.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.login.received.ReceivedNewId;
import com.vethrfolnir.game.network.login.received.ServerKilled;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class LoginServerClientHandler extends ChannelInboundHandlerAdapter {
	private static final MuLogger log = MuLogger.getLogger(LoginServerClientHandler.class);
	
	/** Server killed by login **/
	public static final ServerKilled ServerKill = new ServerKilled();

	/** Server received a new id from login **/
	public static final ReceivedNewId RecievedAlternativeId = new ReceivedNewId();

	private final LoginServerClient serverClient;

	/**
	 * @param serverClient
	 */
	public LoginServerClientHandler(LoginServerClient serverClient) {
		this.serverClient = serverClient;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (ByteBuf)msg;
		
		int opcode = buff.readUnsignedByte();
		
		switch (opcode) {
			case 0xBB:
				ServerKill.read(serverClient, buff);
				break;
			case 0xA1:
				int newId = buff.readInt();
				RecievedAlternativeId.read(serverClient, buff, newId);
				break;
			default:
				log.warn("Unknown packet 0x" + PrintData.fillHex(opcode, 2) + ". Dump: "+PrintData.printData(buff.nioBuffer(0, buff.writerIndex())));
				break;
		}
		
		buff.release();
	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		serverClient.create(ctx);
		serverClient.sendPacket(LoginPackets.ServerAuthPacket);
	}

}
