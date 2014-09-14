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
import java.util.concurrent.atomic.AtomicBoolean;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.crypt.MuCryptUtils;
import com.vethrfolnir.game.network.mu.packets.MuReadPacket;
import com.vethrfolnir.game.network.mu.send.ExClientAuthAnswer.AuthResult;
import com.vethrfolnir.game.services.dao.AccountDAO;
import com.vethrfolnir.game.templates.AccountInfo;

import corvus.corax.processing.annotation.Config;
import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 *
 */
public final class ExRequestAuth extends MuReadPacket {

	@Config(key = "Client.Version", value = "No Version")
	private String version;

	@Config(key = "Client.Serial", value = "No Serial")
	private String serial;
	
	@Config(key = "Game.Capacity", value = "100")
	private int capacity;
	
	@Config(key = "Account.AutoCreate", value = "false")
	private boolean autoCreateAccount;
	
	@Inject
	private EntityWorld entityWorld;
	
	@Override
	public void read(MuClient client, ByteBuf buff, Object... params) {
		
		if(MuNetworkServer.onlineClients() >= capacity) {
			invalidate(buff);
			
			client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.ServerIsFull);
			return;
		}
		
		byte[] data1 = new byte[10];
		byte[] data2 = new byte[10];

		buff.getBytes(4, data1);
		buff.getBytes(14, data2);

		MuCryptUtils.Dec3bit(data1, 0, 10);
		MuCryptUtils.Dec3bit(data2, 0, 10);
		
		buff.setBytes(4, data1);
		buff.setBytes(14, data2);

		buff.readerIndex(4);
		
		String user = readS(buff, 10);
		String password = readS(buff, 10);

		buff.readerIndex(38);
		
		String version = readS(buff, 5);
		String mainSerial = readS(buff, 16);
		System.out.println("user: [" + user + "] pass[" + password + "] - "+" Version:["+version+"] "+" Serial: ["+mainSerial+"]");
		
		enqueue(client, user, password, version, mainSerial);
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#invokeLater(java.lang.Object[])
	 */
	@Override
	public void invokeLater(Object... buff) {
		final MuClient client = as(buff[0]);
		final String accountName = as(buff[1]);
		String password = as(buff[2]);
		
		if(buff[3].equals(version) && buff[4].equals(serial)) {
			AccountDAO dao = DatabaseAccess.AccountAccess();

			AccountInfo info = dao.getInfo(accountName);

			if(info == null) {
				
				if(autoCreateAccount) {
					client.auth(accountName);
					dao.createAccount(accountName, password, client);
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.AuthSucceeded);
				}
				else
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.AccountInvalid);
			}
			else {
				
				if(info.accessLevel < 0) {
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.AccountBlocked);
					return;
				}

				final AtomicBoolean alreadyOn = new AtomicBoolean(false);

				entityWorld.getClients().forEach((e) -> {
					MuClient iClient = e.getClient();
					
					if(client == iClient || client.getAccount() == null)
						return; // continue
					
					if(iClient.getAccount().getAccountName().equalsIgnoreCase(accountName)) {
						alreadyOn.set(true);
					}
				});

				if(alreadyOn.get()) {
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.AccountAlredyConnected);
					return;
				}

				try
				{
					MessageDigest md = MessageDigest.getInstance("md5");
			
					byte[] DB = md.digest(password.getBytes("UTF-8"));

					for(int i = 0; i < DB.length; i++) {
						if(DB[i] != info.passwordHash[i]) {
							client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.InvalidPassword);
							return;
						}
					}

					client.auth(accountName);
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.AuthSucceeded);
					dao.update(client);
				}
				catch (Exception e) {
					client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.InvalidPassword);
				}
			}
		}
		else
			client.sendPacket(MuPackets.ExAuthAnswer, AuthResult.WrongVersion);
	}

}
