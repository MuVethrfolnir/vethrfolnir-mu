/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class Endiness {

	public static void main(String[] args) {
		int on = 60;
		System.out.println(Integer.toHexString(on & 0xFF)+ " - "+ Integer.toHexString(on >> 8));

		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putInt(on);
		buff.flip();
		
		System.out.println(PrintData.printData(buff));
	}
}
