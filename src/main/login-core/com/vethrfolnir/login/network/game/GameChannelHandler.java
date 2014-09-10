/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login.network.game;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.login.network.NetworkGameServer;
import com.vethrfolnir.login.network.game.received.RequestServerRegistration;
import com.vethrfolnir.login.services.GameNameService;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Inject;
import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class GameChannelHandler extends ChannelInboundHandlerAdapter {

	private static final MuLogger log = MuLogger.getLogger(GameChannelHandler.class);
	
	@Inject
	private GameNameService nameService;
	
	@SuppressWarnings("unused")
	private final NetworkGameServer netInterface;
	
	/**
	 * @param nameService
	 * @param context
	 */
	private static final RequestServerRegistration ServerRegistration = new RequestServerRegistration();
	
	/**
	 * @param networkGameServer
	 */
	public GameChannelHandler(NetworkGameServer networkGameServer) {
		netInterface = networkGameServer;
		Corax.pDep(this);
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		nameService.getServer(ctx.channel()).remove();
	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (ByteBuf)msg;

		int opcode = buff.readUnsignedByte();
		
		switch (opcode) {
			case 0x0A: // first registration
				ServerRegistration.read(null, buff, nameService, ctx);
				break;
			case 0x0B: {
				nameService.getServer(ctx.channel()).setOnlinePlayers(buff.readInt());;
			}
			default:
				log.warn("Unknown packet[opcode = 0x"+PrintData.fillHex(opcode, 2)+"]");
				break;
		}
		buff.release();
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		if(cause instanceof IOException) {
			MuLogger.getLogger("netty.io.exception").warn("IO Exception in Netty!", cause);;
			return;
		}
		
		log.fatal("Uncaught exception!", cause);
	}

}
