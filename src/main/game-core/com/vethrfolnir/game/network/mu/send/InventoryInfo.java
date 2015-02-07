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
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class InventoryInfo extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		writeArray(buff, 0xC4, 0x00, 0x2D, 0xF3, 0x10);
		
		GameObject e = as(params[0]) == null ? ((MuClient)context).getEntity() : (GameObject) params[0];
		Inventory inv = e.get(CreatureMapping.Inventory);
		
		writeC(buff, inv.itemSize());
		for (int i = 0; i < inv.getItems().size(); i++) {
			MuItem item = inv.getItems().get(i);
			
			if(item == null)
				continue;
			
			writeC(buff, item.getSlot());
			writeArray(buff, item.toCode());
		}
		
		//writeArray(buff, 0x03, 0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x10, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x0C, 0x14, 0x08, 0x1E, 0x00, 0x00, 0xD0, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x0D, 0x14, 0x10, 0x1E, 0x00, 0x00, 0xD0, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF); // content
	}

}
