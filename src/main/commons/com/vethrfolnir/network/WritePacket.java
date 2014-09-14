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

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.tools.Tools;

/**
 * @author Vlad
 * All packets should be managed as static instances!
 */
public abstract class WritePacket {
	
	protected static final MuLogger log = MuLogger.getLogger(WritePacket.class); 
	
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
