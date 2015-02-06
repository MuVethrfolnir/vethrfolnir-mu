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
package com.vethrfolnir.inv;



/**
 * @author Vlad
 *
 */
public class Inv {

	public static class InvItem {
		int x, y;
		int w, h;

		public InvItem(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}
	
	public static void main(String[] args) {
		boolean[][] slots = new boolean[8][8];
		
		InvItem itm1 = new InvItem(0, 3, 2, 4);
		put(slots, itm1);

		InvItem itm2 = new InvItem(0, 0, 2, 4);
		
		System.out.println("Can i? "+check(slots, itm2));
		if(!check(slots, itm2)) {
			if(findCloses(slots, itm2))
				put(slots, itm2);
		}
		
		for (int i = 0; i < slots.length; i++) {
			boolean[] row = slots[i]; // y
			for (int j = 0; j < row.length; j++) {
				boolean col = row[j]; // x
				if(col)
					System.out.print("X ");
				else
					System.out.print("O ");
			}
			System.out.println();
		}
	}


	private static boolean findCloses(boolean[][] slots, InvItem itm) {
		for (int i = 0; i < slots.length; i++) {
			boolean[] rows = slots[i];
			for (int j = 0; j < rows.length; j++) {
				itm.x = i;
				itm.y = j;
				
				if(check(slots, itm))
					return true;
			}
		}
		
		return false;
	}

	private static void put(boolean[][] slots, InvItem itm) {
		if(check(slots, itm)) {
			slots[itm.y][itm.x] = true;
			for (int i = 0; i < itm.h; i++) {
				int row = itm.y + i;
				slots[row][itm.x] = true;
				for (int j = 0; j < itm.w; j++) {
					slots[row][itm.x + j] = true;
				}
			}
			
			int height = 8, width = 8;
			int slot = (itm.y % height) * width + (itm.x % width);
			System.out.println("Placed item at position slot: "+slot);
		}
	}

	private static boolean check(boolean[][] slots, InvItem itm) {
		
		if(itm.y + itm.h > slots.length)
			return false;
		
		if(itm.x + itm.w > slots[itm.y].length)
			return false;
		
		if(!slots[itm.y][itm.x]) {
			for (int i = 0; i < itm.h; i++) {
				int row = itm.y + i;
				if(!slots[row][itm.x]) {
					for (int j = 0; j < itm.w; j++) {
						if(slots[row][itm.x + j])
							return false;
					}
				}
				else
					return false;
			}
		}
		else
			return false;

		
		return true;
	}
}
