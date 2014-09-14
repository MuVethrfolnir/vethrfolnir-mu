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
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.services.IdFactory;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.staticdata.ClassId;
import com.vethrfolnir.game.templates.AccountCharacterInfo;

import corvus.corax.processing.annotation.Inject;
import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class RequestCharacterCreate extends MuReadPacket {

	@Inject
	private IdFactory idFactory;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		AccountCharacterInfo info = new AccountCharacterInfo();
		
		info.name = readS(buff, 10);
		info.classId = readC(buff);
		
		if(!ClassId.isValid(info.classId)) {
			log.warn("Client["+client+"] sent invalid character create packet!");
			client.close();
		}
		
		System.out.println("Creating char["+info.name+"] classId: ["+info.classId+" Hex: "+PrintData.fillHex(info.classId, 2)+"]");

		AccountDAO dao = DatabaseAccess.AccountAccess();
		boolean isNameFree = dao.isNameFree(info.name);
		int slot = client.getAccount().obtainSlot();
		
		if(isNameFree || slot == -1) {
			client.getAccount().free(slot);
			client.sendPacket(MuPackets.CharacterCreateAnswer, false, null);
			return;
		}

		info.charId = idFactory.obtain();
		info.level = 1;
		info.slot = slot;

		client.sendPacket(MuPackets.CharacterCreateAnswer, true, info);
		dao.createCharacter(client.getAccount().getAccountName(), info, 0, 127, 127);
		client.getAccount().addCharacter(info);
	}
}
