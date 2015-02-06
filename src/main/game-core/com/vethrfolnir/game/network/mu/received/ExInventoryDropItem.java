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
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.logging.MuLogger;


/**
 * @author Seth
 */
public class ExInventoryDropItem extends MuReadPacket {
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		int x = readC(buff);
		int y = readC(buff);

		Inventory inventory = client.getEntity().get(CreatureMapping.Inventory);
		int itemSlot = readC(buff) - inventory.getType().getOffset();

		MuItem item = inventory.getItem(itemSlot);

		if(item == null) // Error or hack
		{
			MuLogger.e(getClass().getSimpleName()+": Character["+client+"] requested to move an inexistent item on slot["+itemSlot+"].");
			client.sendPacket(MuPackets.InventoryInfo, client.getEntity());
		}
		else {
			
			System.out.println("Droping item: "+item.getName());
			inventory.removeItem(itemSlot);

			/**
			 * TODO
			 * client.sendPacket(MuPackets.ExInventoryDeleteItem(itemSlot));

			
			item.showItem(player, player.getMapId(), _x, _y);
			item.setPendingDelete(true);
			player.getInventory().updateDB(item);
			*/
		}
	}

}
