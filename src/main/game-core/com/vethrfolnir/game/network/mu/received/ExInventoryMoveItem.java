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
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.entitys.components.inventory.WindowType;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.logging.MuLogger;

/**
 * @author Seth
 */
public class ExInventoryMoveItem extends MuReadPacket {

	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		
		MuItem item = null;
		byte[] data = readByteArray(buff);

		int oldWin = data[0] & 0xFF;
		int oldPos = data[1] & 0xFF;

		int newWin = data[data.length - 2] & 0xFF;
		int newPos = data[data.length - 1] & 0xFF;

		
		WindowType newWindow = WindowType.values()[newWin];
		WindowType oldWindow = WindowType.values()[oldWin];
		
		newPos = newPos - newWindow.getOffset();
		oldPos = oldPos - oldWindow.getOffset();
		
		Inventory inventory = client.getEntity().get(CreatureMapping.Inventory);

		System.out.println("Request place item: "+newPos+" from "+oldPos+ " newin "+newWindow+ " from old: "+oldWindow);

		switch (oldWindow) {
			case InventoryWindow:
				item = inventory.getItem(oldPos);
				System.out.println("Getting from inventory");
				break;
			case VaultWindow: //TODO:  when adding and removing from vault, update the zen
				//_item = inventory.getVault()).getItem(oldPos);
				System.out.println("Getting from vault");
				break;
			default:
				MuLogger.e(oldWindow.name()+" not handled yet");
				break;
		}

		if(item == null) {
			MuLogger.e(getClass().getSimpleName()+": Character["+client+"] requested to move an inexistent item.");
			return;
		}

		inventory.move(item, oldPos, newPos, newWindow);
	}

}
