/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login.network.mu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.ByteOrder;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.login.network.mu.send.SendServerInfo;
import com.vethrfolnir.login.network.mu.send.SendServerLists;
import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Inject;
import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

	private static final MuLogger log = MuLogger.getLogger(ClientChannelHandler.class);
	
	@Inject
	private GameNameService nameService;
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		if(nameService.liveServerSize() > 0) {
			ByteBuf buff =  ctx.alloc().buffer(4);
			ctx.writeAndFlush(buff.writeBytes(new byte[] {(byte)0xC1, 0x04, 0x00, 0x01}));
		}
	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (ByteBuf)msg;

		// we are not interested in the header and size;
		buff.readerIndex(2);
		
		int opcode = buff.readUnsignedShort();

		switch (opcode) {

			case 0xf4_03: { // Request Server information
				int serverId = buff.readUnsignedByte();
				ByteBuf buf = ctx.alloc().heapBuffer().order(ByteOrder.LITTLE_ENDIAN);
				
				WritePacket packet = SendServerInfo.StaticPacket;
				packet.write(null, buf, nameService, serverId);
				packet.markLength(buf);;
				
				ctx.writeAndFlush(buf);
				break;
			}
			case 0xf4_06: { // Request Server list
				ByteBuf buf = ctx.alloc().heapBuffer().order(ByteOrder.LITTLE_ENDIAN);

				WritePacket packet =  SendServerLists.StaticPacket;
				packet.write(null, buf, nameService);
				packet.markLength(buf);
				
				ctx.writeAndFlush(buf);
				break;
			}
			default:
				log.warn("Unkown packet[OPCODE = "+Integer.toHexString(opcode)+"] Dump: "+PrintData.printData(buff.nioBuffer(0, buff.writerIndex())));
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

	public ClientChannelHandler() { Corax.pDep(this); }
}
