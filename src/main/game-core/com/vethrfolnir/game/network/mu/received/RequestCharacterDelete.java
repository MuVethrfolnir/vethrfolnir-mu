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

import java.security.MessageDigest;

import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.services.IdFactory;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public class RequestCharacterDelete extends MuReadPacket {

	@Inject
	private IdFactory idFactory;

	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		String name = readS(buff, 10);
		String pws = readS(buff, 10);

		AccountDAO dao = DatabaseAccess.AccountAccess();

		byte[] pwsHash = dao.getPassword(client.getAccount().getAccountName());

		if(pwsHash == null) // Smth wierd i cant stop thinking about
			client.close();
		
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			
			byte[] DB = md.digest(pws.getBytes("UTF-8"));

			for(int i = 0; i < DB.length; i++) {
				if(DB[i] != pwsHash[i]) {
					client.sendPacket(MuPackets.CharacterDeleteAnswer, false);
					return;
				}
			}

			AccountCharacterInfo character = client.getAccount().getLobbyCharacter(name);
			client.getAccount().free(character.slot);
			idFactory.free(character.charId);

			client.sendPacket(MuPackets.CharacterDeleteAnswer, true);
			dao.deleteCharacter(name);
		}
		catch(Exception e) {
			MuLogger.e("Failed deleting character!", e);
			client.sendPacket(MuPackets.CharacterDeleteAnswer, false);
		}
	}

}
