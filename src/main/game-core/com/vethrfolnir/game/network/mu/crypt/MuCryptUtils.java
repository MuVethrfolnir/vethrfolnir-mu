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
package com.vethrfolnir.game.network.mu.crypt;

import corvus.corax.tools.PrintData;
import io.netty.buffer.ByteBuf;

/**
 * @author Vlad
 *
 */
public class MuCryptUtils {

    static int GetDecodedSize(ByteBuf buff)
    {
    	switch(buff.getUnsignedByte(0))
    	{
	    	case 0xC1:
	    	    return GetPacketSize(buff);
	    	case 0xC3:
	    		return (((GetPacketSize(buff) - 2) * 8) / 11) + 1;
	    	case 0xC2:
	    	    return GetPacketSize(buff);
	    	case 0xC4:
	    		return (((GetPacketSize(buff) - 3) * 8) / 11) + 2;
	    	default:
	    		return 0;
    	}
    }

	static int GetHeaderSize(ByteBuf buff)
	{
		switch(buff.getUnsignedByte(0))
		{
			case 0xC1:
			case 0xC3:
				return 2;
			case 0xC2:
			case 0xC4:
				return 3;
		}
		return -1;
	}
	
	static int GetPacketSize(ByteBuf buff)
	{
		switch(buff.getUnsignedByte(0))
		{
			case 0xC1:
			case 0xC3:
				return buff.getUnsignedByte(1);
			case 0xC2:
			case 0xC4:
				return buff.getUnsignedByte(1) << 8 | buff.getUnsignedByte(2) & 0xFF;
		}
		return -1;
	}
	
    static int GetDecodedSize(byte[] buff)
    {
    	switch(buff[0] & 0xFF)
    	{
	    	case 0xC1:
	    	    return GetPacketSize(buff);
	    	case 0xC3:
	    		return (((GetPacketSize(buff) - 2) * 8) / 11) + 1;
	    	case 0xC2:
	    	    return GetPacketSize(buff);
	    	case 0xC4:
	    		return (((GetPacketSize(buff) - 3) * 8) / 11) + 2;
	    	default:
	    		return 0;
    	}
    }

	static int GetHeaderSize(byte[] buff)
	{
		switch(buff[0] & 0xFF)
		{
			case 0xC1:
			case 0xC3:
				return 2;
			case 0xC2:
			case 0xC4:
				return 3;
		}
		return -1;
	}
	
	static int GetPacketSize(byte[] buff)
	{
		switch(buff[0] & 0xFF)
		{
			case 0xC1:
			case 0xC3:
				return buff[1] & 0xFF;
			case 0xC2:
			case 0xC4:
				return ((buff[1] & 0xFF) & 256) + (buff[2] & 0xFF);
		}
		return -1;
	}
	
	public static final long readRing(ByteBuf converter, short[] shift) {
		for (int i = 0; i < shift.length; i++) {
			converter.writeByte(shift[i]);
		}
		
		long uInt = converter.readInt();
		converter.clear();
		return uInt;
	}
	
	public static final short[] getAsUByteArray(ByteBuf buff) {
		short[] uByteArray = new short[buff.capacity()];
		for (int i = 0; i < uByteArray.length; i++) {
			uByteArray[i] = buff.getUnsignedByte(i);
		}
		return uByteArray;
	}

	public static final short[] getAsUByteArray(ByteBuf buff, short[] uByteArray) {
		for (int i = 0; i < uByteArray.length; i++) {
			uByteArray[i] = buff.getUnsignedByte(i);
		}
		return uByteArray;
	}

	public static final void readAsUByteArray(ByteBuf buff, short[] uByteArray) {
		for (int i = 0; i < uByteArray.length; i++) {
			uByteArray[i] = buff.readUnsignedByte();
		}
	}

	public static final void readAsUByteArray(ByteBuf buff, short[] uByteArray, int len) {
		for (int i = 0; i < len; i++) {
			uByteArray[i] = buff.readUnsignedByte();
		}
	}

	public static final short[] writeByteArray(ByteBuf buff, short[] uByteArray) {
		for (int i = 0; i < uByteArray.length; i++) {
			buff.writeByte(uByteArray[i]);
		}
		return uByteArray;
	}

	public static void flushArray(short[] arr, int index, int length)
    {
    	for (int i = index; i < length; i++) {
			arr[i] = 0;
		}
    }
    
	private static final byte[] xor3Bytes = { (byte) 0xFC, (byte) 0xCF, (byte) 0xAB };

	public static void Dec3bit(byte[] array, int start, int len) 
	{
		for (int i = start; i < start + len; i++) {
			array[i] = (byte) (array[i] ^ xor3Bytes[(i - start) % 3]);
		}
	}
    
	/**
	 * @param string
	 * @return
	 */
	public static byte[] toByte(String string) {
		byte[] data = new byte[string.length() / 2];
		
		int loc = 0;
		for (int i = 0; i < string.length(); i += 2) {
			int hex = Integer.decode("0x"+string.charAt(i)+""+string.charAt(i + 1));
			data[loc++] = (byte) hex;
		}
		
		return data;
	}

	/**
	 * @param data
	 * @return
	 */
	public static String toString(byte[] data) {
		String res = "";
		for (int i = 0; i < data.length; i++) {
			 res += PrintData.fillHex(data[i] & 0xff, 2);
		}
		
		return res;
	}
}
