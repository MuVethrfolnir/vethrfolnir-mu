/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.network;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.tools.Tools;

/**
 * @author Vlad
 * All packets should be managed as static instances!
 */
public abstract class WritePacket {
	
	private static final MuLogger log = MuLogger.getLogger(WritePacket.class); 
	
	public abstract void write(NetworkClient context, ByteBuf buff, Object... params);
	
	public WritePacket() {
		Corax.pDep(this);
	}
	
	public void writeC(ByteBuf buff, int value) {
		buff.writeByte(value);
	}
	
	public void writeD(ByteBuf buff, int value) {
		buff.writeInt(value);
	}
	
	public void writeSh(ByteBuf buff, int value) {
		buff.writeShort(value);
	}
	
	public void writeD(ByteBuf buff, int value, ByteOrder order) {
		ByteBuf buf = buff.alloc().buffer(4,4).order(order);
		buf.writeInt(value);
		buff.writeBytes(buf);
		buf.release();
	}
	
	public void writeSh(ByteBuf buff, int value, ByteOrder order) {
		ByteBuf buf = buff.alloc().buffer(2,2).order(order);
		buf.writeShort(value);
		buff.writeBytes(buf);
		buf.release();
	}
	
	public void writeArray(ByteBuf buff, int... vals) {
		for (int i = 0; i < vals.length; i++) {
			buff.writeByte(vals[i]);
		}
	}
	
	public void writeArray(ByteBuf buff, byte... vals) {
		for (int i = 0; i < vals.length; i++) {
			buff.writeByte(vals[i]);
		}
	}
	
	public void writeArray(ByteBuf buff, int fills, byte... vals) {
		for (int i = 0; i < vals.length; i++) {
			buff.writeByte(vals[i]);
		}
		
		for(int i = vals.length; i < fills; i++)
			buff.writeByte(0x00);
	}
	/**
	 * This is only for LS <-> GS Communication, do not use it for clients!
	 * @param buff
	 * @param value
	 */
	public void writeS(ByteBuf buff, String value) {
		if(value == null)
			throw new RuntimeException("Value is null!");
		
		try {
			for (int i = 0; i < value.length(); i++) {
				buff.writeChar(value.charAt(i));
			}
			buff.writeChar('\000');
		}
		catch (Exception e) {
			log.warn("Failed writing string!", e);
		}
	}

	public void writeNS(ByteBuf buff, String value) {
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

	@SuppressWarnings("unchecked")
	public <T> T as(Object obj) {
		return (T)obj;
	}

	/**
	 * This will also parse strings into the needed type
	 * @param obj
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T as(Object obj, Class<T> type) {
		if(type.isInstance(obj)) {
			return (T)obj;
		}

		if(obj instanceof String)
			return (T)Tools.parsePrimitiveTypes(type, String.valueOf(obj));
		
		return (T)obj;
	}
	
	public void markLength(ByteBuf buff) {
		int lenght = buff.writerIndex();
		switch (buff.getUnsignedByte(0)) {
			case 0xC1:
			case 0xC3:
				buff.setByte(1, lenght);
				break;
			case 0xC2:
			case 0xC4:
				buff.setByte(1, lenght >> 8);
				buff.setByte(2, lenght & 0xFF);
				break;
		}
	}
	
	public boolean isEncryptable(ByteBuf buff)
	{
		switch (buff.getUnsignedByte(0)) {
			case 0xC3:
			case 0xC4:
				return true;
		}
		
		return false;
	}
	
}
