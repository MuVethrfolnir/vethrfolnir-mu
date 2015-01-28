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

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.module.CombatEngine;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.staticdata.world.Region;

import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public class RequestUseSkill extends MuReadPacket {

	@Inject
	private EntityWorld entityWorld;
	
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		int skillId = readSh(buff, ByteOrder.BIG_ENDIAN);
		int targetId = readSh(buff, ByteOrder.BIG_ENDIAN);
		
		GameObject entity = entityWorld.getEntitys().get(targetId);
		
		if(entity == null)
			return;
		
		Region ap = client.getEntity().get(CreatureMapping.Positioning).getCurrentRegion();
		Region bp = entity.get(CreatureMapping.Positioning).getCurrentRegion();

		if(ap != bp) {
			log.info("Client["+client+"] tryed to attack an target["+targetId+"] thats not in the same region."); // packet hack, by remembering the objId
			client.close();
			return;
		}

		CombatEngine.useSkill(client.getEntity(), skillId, entity);
	}

}
