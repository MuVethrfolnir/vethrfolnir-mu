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
