/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import java.security.MessageDigest;

import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.services.IdFactory;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 *
 */
public class RequestCharacterDelete extends ReadPacket {

	@Inject
	private IdFactory idFactory;

	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		String name = readS(buff, 10);
		String pws = readS(buff, 10);

		AccountDAO dao = DatabaseAccess.AccountAccess();

		MuClient client = as(context, MuClient.class);
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
