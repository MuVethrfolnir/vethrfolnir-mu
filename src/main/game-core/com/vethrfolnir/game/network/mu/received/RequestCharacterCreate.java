/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.services.IdFactory;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.staticdata.ClassId;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.processing.annotation.Inject;
import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class RequestCharacterCreate extends ReadPacket {

	@Inject
	private IdFactory idFactory;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = as(context, MuClient.class);
		AccountCharacterInfo info = new AccountCharacterInfo();
		
		info.name = readConcatS(buff, 10, 0x00);
		info.classId = readC(buff);
		
		if(!ClassId.isValid(info.classId))
			client.clean();
		
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
