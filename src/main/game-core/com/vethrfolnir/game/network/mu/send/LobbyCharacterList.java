/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.util.List;

import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.module.MuAccount;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class LobbyCharacterList extends WritePacket {
	
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {

		MuAccount account = as(context, MuClient.class).getAccount();
		AccountDAO dao = DatabaseAccess.AccountAccess();
		
		int count = dao.getLobbyCharacterSize(account.getAccountName()); // character count
		
		boolean rf = CorvusConfig.getProperty("ForceAllowRageFighter", false);
		boolean mg = CorvusConfig.getProperty("ForceAllowMagicGladiator", false);
		boolean dl = CorvusConfig.getProperty("ForceAllowDarkLord", false);
		
		int allowed = 0x01; // default 0
		// calculate here how many to show
		
		// forces
		if(rf)
			allowed = 0x01;
		
		if(mg)
			allowed = 0x04;
		
		if(dl)
			allowed = 0x05;

		System.out.println(getClass().getSimpleName()+" : sending "+count+" characters for "+account.getAccountName()+" account.");
		
		if(count == 0) {
			writeArray(buff, 0xC1, 0x18, 0xF3, 0x00, allowed, 0x00, 0x00/** size**/, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
			return;
		}
		
		List<AccountCharacterInfo> characters = account.getCharacterList();
		
		// static
		writeArray(buff, 0xC1, 0x00, 0xF3, 0x00, allowed, 0x00, count, 0x00);
		for (int i = 0; i < characters.size(); i++) {
			AccountCharacterInfo info = characters.get(i);

			writeC(buff, info.slot); // position - starts from 0
			writeS(buff, info.name, 10);
			writeC(buff, 0x00); // Name Split
			writeSh(buff, info.level); // big endien
			writeC(buff, info.access >= 20 ? 0x20 : 0x00); // Ctl Code :D aka access lvl
			writeC(buff, info.classId << 1);
			writeArray(buff, info.wearBytes);
			writeC(buff, 0xFF); // guild Status
		}
	}

}
