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
