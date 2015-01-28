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
import com.vethrfolnir.tools.PrintData;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;

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

	public ClientChannelHandler() { Corax.process(this); }
}
