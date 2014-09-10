/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.login.received.ReceivedNewId;
import com.vethrfolnir.game.network.login.received.ServerKilled;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.tools.PrintData;

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
