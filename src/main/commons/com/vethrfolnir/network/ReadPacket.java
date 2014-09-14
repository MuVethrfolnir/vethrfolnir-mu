/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public abstract class ReadPacket {
	protected static final MuLogger log = MuLogger.getLogger(ReadPacket.class);
	
	public abstract void read(NetworkClient context, ByteBuf buff, Object... params);
	
	public ReadPacket() {
		Corax.pDep(this);
	}

	protected short readC(ByteBuf buff) {
		return buff.readUnsignedByte();
	}
	
	protected int readD(ByteBuf buff) {
		return buff.readInt();
	}
	
	protected int readSh(ByteBuf buff) {
		return buff.readUnsignedShort();
	}
	
	/**
	 * Don't forget to release the buffer
	 * @param buff
	 * @return
	 */
	protected ByteBuf readArray(ByteBuf buff) {
		return buff.readBytes(buff.readableBytes());
	}

	/**
	 * Don't forget to release the buffer
	 * @param buff
	 * @param len
	 * @return
	 */
	protected ByteBuf readArray(ByteBuf buff, int len) {
		return buff.readBytes(len);
	}
	
	/**
	 * This is only for LS <-> GS Communication, do not use it for clients! Unless overriden and managed
	 * @param buff
	 * @return
	 */
	protected String readS(ByteBuf buff) {
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

	@SuppressWarnings("unchecked")
	protected <T> T as(Object obj) {
		return (T)obj;
	}

	@SuppressWarnings("unchecked")
	protected <T> T as(Object obj, Class<T> type) {
		return (T)obj;
	}
	
	protected void invalidate(ByteBuf buff) {
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
