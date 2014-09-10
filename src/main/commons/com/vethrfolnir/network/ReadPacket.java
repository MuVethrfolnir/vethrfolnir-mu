/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.network;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public abstract class ReadPacket {
	private static final MuLogger log = MuLogger.getLogger(ReadPacket.class);
	
	public abstract void read(NetworkClient context, ByteBuf buff, Object... params);
	
	public ReadPacket() {
		Corax.pDep(this);
	}

	protected final short readC(ByteBuf buff) {
		return buff.readUnsignedByte();
	}
	
	protected final int readD(ByteBuf buff) {
		return buff.readInt();
	}
	
	protected final int readSh(ByteBuf buff) {
		return buff.readUnsignedShort();
	}
	
	protected final int readD(ByteBuf buff, ByteOrder order) {
		ByteBuf buf = buff.alloc().buffer(4,4).order(order);
		buf.writeBytes(buff.readBytes(buf));
		
		int rezult = buf.readInt();
		buf.release();
		
		return rezult;
	}
	
	protected final long writeSh(ByteBuf buff, ByteOrder order) {
		ByteBuf buf = buff.alloc().buffer(2,2).order(order);
		buf.writeBytes(buff.readBytes(buf));
		
		long rezult = buf.readUnsignedShort();
		buf.release();

		return rezult;
	}
	
	/**
	 * Don't forget to release the buffer
	 * @param buff
	 * @return
	 */
	protected final ByteBuf readArray(ByteBuf buff) {
		return buff.readBytes(buff.readableBytes());
	}

	/**
	 * Don't forget to release the buffer
	 * @param buff
	 * @param len
	 * @return
	 */
	protected final ByteBuf readArray(ByteBuf buff, int len) {
		return buff.readBytes(len);
	}
	
	/**
	 * This is only for LS <-> GS Communication, do not use it for clients!
	 * @param buff
	 * @return
	 */
	protected final String readS(ByteBuf buff) {
		try {
			ArrayList<Character> ins = new ArrayList<>();
			
			char in;
			while(buff.isReadable() && (in = buff.readChar()) != '\000') {
				ins.add(in);
			}
			
			char[] arr = new char[ins.size()];
			
			for (int i = 0; i < arr.length; i++) {
				arr[i] = ins.get(i);
			}
			String str = new String(arr);
			return str;
		}
		catch (Exception e) {
			log.warn("Failed reading string!", e);
		}
		
		return null;
	}

	protected final String readS(ByteBuf buff, int max) {
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

	protected final String readConcatS(ByteBuf buff, int max, int offCode) {
		try {
			ByteBuf copy = buff.readBytes(max);
			String str = new String(copy.array(), "ISO-8859-1");
			
			if (str.indexOf(offCode) != -1)
				str = str.substring(0, str.indexOf(offCode));
			
			copy.release();
			return str;
		}
		catch (UnsupportedEncodingException e) {
			log.warn("Failed reading string!", e);
		}

		return null;
	}

	protected final void shiftC(ByteBuf buff, int pos) {
		byte b = buff.array()[pos];
		b &= 0x7F;
		b |= 0x80;
		buff.array()[pos] = b;
	}

	@SuppressWarnings("unchecked")
	protected final <T> T as(Object obj) {
		return (T)obj;
	}

	@SuppressWarnings("unchecked")
	protected final <T> T as(Object obj, Class<T> type) {
		return (T)obj;
	}
	
	protected final void invalidate(ByteBuf buff) {
		buff.readerIndex(buff.writerIndex());
	}
	
	/**
	 * Execute later in the cached thread pool
	 * @param run
	 */
	protected final void enqueue(Runnable run) {
		Corax.getInstance(CorvusThreadPool.class).executeLongRunning(run);
	}
	
	protected final void enqueue(final Object... buff) {
		Corax.getInstance(CorvusThreadPool.class).executeLongRunning(new Runnable() {
			
			@Override
			public void run() {
				ReadPacket.this.invokeLater(buff);
			}
		});
	}
	
	/**
	 * Override if enqueued
	 * @param buff
	 */
	protected void invokeLater(Object... buff) { throw new RuntimeException("ReadPacket.invokeLater() must be overriden when using an enqueue()!"); }
}
