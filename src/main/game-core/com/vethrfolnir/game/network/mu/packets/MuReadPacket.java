/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 *
 */
public abstract class MuReadPacket extends ReadPacket {

	public abstract void read(MuClient client, ByteBuf buff, Object... params);
	
	@Override
	public final void read(NetworkClient context, ByteBuf buff, Object... params) {
		read((MuClient)context, buff, params);
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#readS(io.netty.buffer.ByteBuf)
	 */
	@Override
	protected String readS(ByteBuf buff) {
		return readS(buff, buff.readableBytes());
	}

	protected String readS(ByteBuf buff, int max) {
		try {
			ByteBuf copy = buff.readBytes(max);
			String str = new String(copy.array(), "ISO-8859-1");
			copy.release();
			return str.trim();
		}
		catch (UnsupportedEncodingException e) {
			log.warn("Failed reading string!", e);
		}

		return null;
	}

	protected int readD(ByteBuf buff, ByteOrder order) {
		switch (order.toString()) {
			case "BIG_ENDIAN": {
				int a = buff.readUnsignedByte() << 8;
				int b = buff.readUnsignedByte() << 16;
				int c = buff.readUnsignedByte() << 32;
				int d = buff.readUnsignedByte() & 0xFF;
				return (a | b | c | d);
			}
			case "LITTLE_ENDIAN": {
				int a = buff.readUnsignedByte() & 0xFF;
				int b = buff.readUnsignedByte() << 32;
				int c = buff.readUnsignedByte() << 16;
				int d = buff.readUnsignedByte() << 8;
				return (a | b | c | d);
			}
		}
		
		throw new RuntimeException("Order: "+order+" is not mapped!");
	}
	
	protected int readSh(ByteBuf buff, ByteOrder order) {
		
		switch (order.toString()) {
			case "BIG_ENDIAN": {
				int a = buff.readUnsignedByte() << 8;
				int b = buff.readUnsignedByte() & 0xFF;
				return (a | b);
			}
			case "LITTLE_ENDIAN": {
				int a = buff.readUnsignedByte() & 0xFF;
				int b = buff.readUnsignedByte() << 8;
				return (a | b);
			}
		}
		
		throw new RuntimeException("Order: "+order+" is not mapped!");
	}
	

	protected void shiftC(ByteBuf buff, int pos) {
		byte b = buff.array()[pos];
		b &= 0x7F;
		b |= 0x80;
		buff.array()[pos] = b;
	}
}
