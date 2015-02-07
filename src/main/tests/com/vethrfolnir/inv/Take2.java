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

import com.vethrfolnir.game.util.SimpleArray;

/**
 * @author Vlad
 *
 */
public class Take2 {

	private static final int invX = 8, invY = 8, offset = 12;
	
	private static SimpleArray<TItem> items = new SimpleArray<>((invX * invY) + offset);
	
	public static class TItem {
		int slot, width, height;
		public String name;

		public TItem(String name, int width, int height) {
			this.name = name;
			this.width = width;
			this.height = height;
		}
	}
	
	public static void main(String[] args) {
		TItem item = new TItem("Item 1", 1, 6);
		
		put(item, 12, false);
//
//		for (int i = 0; i < 3; i++) {
//			TItem item2 = new TItem("Item "+i, 2, 6);
//			store(item2);
//		}
//		
//		TItem item2 = new TItem("Item "+4, 2, 2);
//		store(item2);
		
		// initial start from the offset
		for (int i = offset; i < items.getCapacity(); i++) {
			
			if(((i - offset) % invX) == 0 && i != offset) {
				System.out.println();
			}
			
			if(items.get(i) == null)
				System.out.print("O ");
			else
				System.out.print("X ");
			
			//System.out.print(i+ " ");
		}
	}

	public static void store(TItem item) {
		for (int i = offset; i < items.getCapacity(); i++) {
			if(check(item, i)) {
				put(item, i, true);
				break;
			}
		}
	}
	
	/**
	 * @param item
	 * @param i
	 * @return 
	 */
	private static boolean check(TItem item, int slot) {
		
		if(item.width > 1 && ((slot + item.width) - offset) % invX == 1)
			return false;

		for (int i = 0; i < item.height; i++) {
			for (int j = 0; j < item.width; j++) {
				int ns = (i * invX + j) + slot;
				
				if (ns >= items.getCapacity())
					return false;
				
				TItem slotItem = items.get(ns);
				
				if(slotItem != null && slotItem != item)
					return false;
			}
		}
		
		return true;
	}

	/**
	 * @param item
	 * @param slot
	 */
	private static void put(TItem item, int slot, boolean force) {
		
		if(force || check(item, slot)) {
			items.set(slot, item);
			item.slot = slot;
			
			for (int i = 0; i < item.height; i++) {
				for (int j = 0; j < item.width; j++) {
					int ns = i * invX + j;
					items.set(slot+ns, item);
				}
			}
		}
		else
			System.out.println("Failed placing item["+item.name+"]");
	}
	
	public static void remove(TItem item) {
		int slot = item.slot;
		items.set(slot, null);
		for (int i = 0; i < item.height; i++) {
			for (int j = 0; j < item.width; j++) {
				int ns = i * invX + j;
				items.set(slot+ns, null);
			}
		}
	}

}
