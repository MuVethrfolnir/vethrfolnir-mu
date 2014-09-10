/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
