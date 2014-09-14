/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.packets;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public abstract class MuWritePacket extends WritePacket {

	public abstract void write(MuClient client, ByteBuf buff, Object... params);
	
	@Override
	public final void write(NetworkClient context, ByteBuf buff, Object... params) {
		write((MuClient)context, buff, params);
	}
	
	public void writeD(ByteBuf buff, int value, ByteOrder order) {
		switch (order.toString()) {
			case "BIG_ENDIAN": {
				buff.writeByte(value >> 8);
				buff.writeByte(value >> 16);
				buff.writeByte(value >> 32);
				buff.writeByte(value & 0xFF);
				break;
			}
			case "LITTLE_ENDIAN": {
				buff.writeByte(value & 0xFF);
				buff.writeByte(value >> 32);
				buff.writeByte(value >> 16);
				buff.writeByte(value >> 8);
			}
		}
	}
	
	public void writeSh(ByteBuf buff, int value, ByteOrder order) {
		switch (order.toString()) {
			case "BIG_ENDIAN": {
				buff.writeByte(value >> 8);
				buff.writeByte(value & 0xFF);
				break;
			}
			case "LITTLE_ENDIAN": {
				buff.writeByte(value & 0xFF);
				buff.writeByte(value >> 8);
			}
		}
	}
	
	@Override
	public void writeS(ByteBuf buff, String value) {
		writeS(buff, value, value.length());
	}
	
	public void writeS(ByteBuf buff, String value, int max) {
		if(value == null)
			throw new RuntimeException("Value is null!");
		
		try {
			int l = value.length();
			buff.writeBytes(value.getBytes("ISO-8859-1"));
			for(int i = l; i < max; i++)
				buff.writeByte(0x00);
		}
		catch (UnsupportedEncodingException e) {
			log.warn("Failed writing string!", e);
		}
	}

	public void shiftC(ByteBuf buff, int pos) {
		byte b = buff.array()[pos];
		b &= 0x7F;
		b |= 0x80;
		buff.array()[pos] = b;
	}
}
