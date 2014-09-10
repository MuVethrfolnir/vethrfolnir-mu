/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu;

import static com.vethrfolnir.game.network.mu.crypt.MuDecoder.DecodePacket;
import static com.vethrfolnir.game.network.mu.crypt.MuDecoder.DecodeXor32;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.vethrfolnir.logging.MuLogger;


/**
 * @author Vlad
 *
 */
public final class MuCyperDecoder extends ByteToMessageDecoder {

	private static final MuLogger _log = MuLogger.getLogger(MuCyperDecoder.class);
	
	/* (non-Javadoc)
	 * @see io.netty.handler.codec.ByteToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List)
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		if(!in.isReadable()) {
			MuClient client = ctx.channel().attr(MuClient.ClientKey).get();
			_log.warn("Client["+client+"] sent an empty packet!");
			
			return; //XXX: is it critical?
		}
		
		if(in.readableBytes() < 3) {
			return; // come back later
		}
		
		in.markReaderIndex();
		
		int opcode = in.readUnsignedByte(); 
		
		int lengthAt = 0;
		switch (opcode) {
			case 0xc1:
			case 0xc3:
				lengthAt = 1;
				break;
			case 0xc2:
			case 0xc4:
				lengthAt = 2;
				break;
		}
		
		//in.markReaderIndex();
		int rez = lengthAt > 1 ? in.readShort() : in.readUnsignedByte();
		in.resetReaderIndex(); 

		//System.out.println("1 Size[point="+(lengthAt > 1 ? "Short" : "Unsigned byte")+"]: "+rez+" opcode "+Integer.toHexString(opcode & 0xFF));
		if (in.readableBytes() < rez) {
			in.resetReaderIndex();
            return;
        }

		int header = in.getUnsignedByte(0);
		if(header == 0xC1 || header == 0xC2) {
			ByteBuf buff = ctx.alloc().heapBuffer(rez);
			in.readBytes(buff);
			out.add(DecodeXor32(buff));
		}
		else {
			out.add(DecodePacket(in));
		}
	}
}
