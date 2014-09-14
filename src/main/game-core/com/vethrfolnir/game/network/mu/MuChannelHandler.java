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
package com.vethrfolnir.game.network.mu;

import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.network.mu.received.*;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Inject;
import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class MuChannelHandler extends ChannelInboundHandlerAdapter {

	private static final MuLogger log = MuLogger.getLogger(MuChannelHandler.class);
	
	private static final TIntObjectHashMap<MuReadPacket> clientpackets = new TIntObjectHashMap<>();
	
	@Inject
	private MuNetworkServer networkServer;
	
	static {
		clientpackets.put(0xF1_01, new ExRequestAuth());
		clientpackets.put(0xF1_02, new RequestLogout());

		// Lobby
		clientpackets.put(0xF3_00, new RequestCharacterList());
		clientpackets.put(0xF3_01, new RequestCharacterCreate());
		clientpackets.put(0xF3_02, new RequestCharacterDelete());
		clientpackets.put(0xF3_15, new RequestCharacterSelect());
		clientpackets.put(0xF3_03, new RequestEnterWorld());
		
		clientpackets.put(0xD4, new MovedToLocation());
		clientpackets.put(0x18, new ValidateStateChange());

		clientpackets.put(0x11, new RequestAttack());

		// Chat
		clientpackets.put(0x00, new RequestSay(false));
		clientpackets.put(0x02, new RequestSay(true));

	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
		log.info("A client"+ctx.channel()+" has connected.");
		
		MuClient client = new MuClient(ctx.channel());
		client.sendPacket(MuPackets.HelloClient);
	}
	
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("A client disconnected "+ctx.channel());
		MuClient client = ctx.channel().attr(MuClient.ClientKey).get();
		client.close();
	}
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (msg instanceof ByteBuffer) ? ctx.alloc().buffer().writeBytes((ByteBuffer)msg) : (ByteBuf)msg;

		buff.readerIndex(2);
		
		int opcode = buff.readUnsignedByte();
		switch (opcode) { // double opcode
			case 0xf1:
			case 0xf3:
			case 0x0e:
			case 0x03:
				buff.readerIndex(buff.readerIndex() - 1);
				opcode = buff.readUnsignedShort(); // ex 0xF1_03
				break;
			default:
				break;
		}
		
		if(opcode == 0xe00) { // Time packet?
			buff.clear();
			buff.release();
			return;
		}

		ReadPacket packet = clientpackets.get(opcode);
		
		if(packet != null) {
			MuClient client = ctx.channel().attr(MuClient.ClientKey).get();
			//System.out.println("Got opcode: 0x"+PrintData.fillHex(opcode, 2)+ " packet = \n"+packet.getClass().getSimpleName());
			packet.read(client, buff);
		}
		else {
			log.warn("Unknown packet[opcode = 0x"+PrintData.fillHex(opcode, 2)+"]. Dump: ");
			log.warn(PrintData.printData(buff.nioBuffer(0, buff.writerIndex())));
		}
		
		//log.warn(PrintData.printData(buff.nioBuffer(0, buff.writerIndex())));
		
		if(buff.refCnt() > 0) {
			//System.out.println("Handler Release when packet[opcode = 0x"+PrintData.fillHex(opcode, 2)+"]");
			buff.release();
		}
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

	public MuChannelHandler() { Corax.pDep(this); }
	
}
