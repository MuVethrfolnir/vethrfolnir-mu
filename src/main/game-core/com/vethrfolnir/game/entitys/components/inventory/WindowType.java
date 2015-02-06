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
package com.vethrfolnir.game.entitys.components.inventory;

/**
 * @author Constantin
 *
 */
public enum WindowType {
	InventoryWindow(8, 8, 12), // 0  offset 12
	TradeWindow(8, 4, 0), // 1
	VaultWindow(8, 15, 0), // 2
	Dummy(0,0, 0), // 3
	StoreWindow(8, 4, 0); // 4

	private int _x, _y, _offset;
	
	private WindowType(int x, int y, int offSet) {
		_x = x;
		_y = y;
		_offset = offSet;
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	public int getOffset() {
		return _offset;
	}
}
