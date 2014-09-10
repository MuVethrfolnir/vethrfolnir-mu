/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.vethrfolnir.game.network.MuNetworkServer;
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
	
	private static final HashMap<Integer, ReadPacket> clientpackets = new HashMap<>(); //TODO Alternative? INT-Object MAP!
	
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
