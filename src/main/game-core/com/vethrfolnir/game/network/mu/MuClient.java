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
package com.vethrfolnir.game.network.mu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.entitys.components.inventory.WindowType;
import com.vethrfolnir.game.entitys.components.player.*;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.module.MuAccount;
import com.vethrfolnir.game.network.mu.crypt.MuEncoder;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.game.templates.PlayerTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public final class MuClient extends NetworkClient {

	private static final MuLogger log = MuLogger.getLogger(MuClient.class);

	//TODO: They did this Attribute thingie to conserve memory, if proves slow cause of a menial thing like that, its out the window!
	public static final AttributeKey<MuClient> ClientKey = AttributeKey.valueOf("MuClient");

	private ClientStatus status = ClientStatus.Connecting;
	
	private int counter;
	
	public enum ClientStatus { Connecting, Authed, InGame }

	private MuAccount account;
	private GameObject entity;

	/**
	 * @param channel
	 */
	public MuClient(Channel channel) {
		super(channel);
		channel.attr(ClientKey).set(this);
		
		EntityWorld world = Corax.fetch(EntityWorld.class);
		entity = world.obtain(this);
	}

	@Inject
	private void initiate() {
		// Prepare getting info from mysql
	}
	
	public void buildPlayer(AccountCharacterInfo info) {
		
		PlayerTemplate template = DatabaseAccess.PlayerAccess().loadPlayer(info.charId);
		entity.setName(template.name);
		
		entity.add(new Positioning(template.x, template.y, template.mapId));
		entity.add(new Inventory(WindowType.InventoryWindow));
		entity.add(new KnownCreatures());
		
		entity.add(new PlayerState(template));
		entity.add(new PlayerStats(template));
		
		entity.commit();
	}
	
	public void clean(boolean light) {
		
		if(!entity.isInitialized()) {
			log.warn("Tried cleaning a non entity["+this+"]!");
			return;
		}
		
		try {
			if(entity.isVoid()) {
				log.warn("Tried saving voided entity["+this+"]!");
				return;
			}
			
			// Perform cleaning
			entity.get(PlayerMapping.Positioning).getCurrentRegion().exit(entity);
			
			DatabaseAccess.PlayerAccess().savePlayer(entity);
			DatabaseAccess.InventoryAccess().writeInventory(entity);
			System.out.println("Saved player!");
		}
		catch(Exception e) {
			log.fatal("Failed cleaning character["+toString()+"]!", e);
		}

		// Goodbye
		entity.destroy(light);
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ClientStatus status) {
		ClientStatus old = this.status;
		this.status = status;

		if(this.status == ClientStatus.Authed && old != ClientStatus.Authed) {
			
			if(entity.isInitialized() && old == ClientStatus.InGame) {
				clean(true);
			}

			account.enteredLobby();
		}
		else
			account.leftLobby();
	}

	/**
	 * @return the status
	 */
	public ClientStatus getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getHostAddress() {
		return channel().remoteAddress().toString();
	}
	
	@Override
	public void close() {
		clean(false);

		channel().close();
	}

	/**
	 * @param counter the counter to set
	 */
	public int getCounter() {
		
		// We need a 0 first
		int ret = counter++;
		
		if(counter >= 256)
			counter = 0;
		
		return ret;
	}

	public void resetCounter() {
		counter = 0;
	}
	
	public int getClientId() {
		return entity.getWorldIndex();
	}

	public void auth(String accountName) {
		account = new MuAccount(this, accountName);
		setStatus(ClientStatus.Authed);
	}
	
	/**
	 * @return the account
	 */
	public MuAccount getAccount() {
		return account;
	}
	
	/**
	 * @param writePacket
	 */
	public void sendPacket(WritePacket writePacket, Object... params) {
		ByteBuf buff = channel().alloc().heapBuffer().order(ByteOrder.LITTLE_ENDIAN);

		writePacket.write(this, buff, params);
		writePacket.markLength(buff);
		
		if(writePacket.isEncryptable(buff)) {
			buff = MuEncoder.EncodePacket(buff, getCounter());
		}
		
		channel().writeAndFlush(buff);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String name = account == null? "I/O" : entity == null ? account.getAccountName() : account.getAccountName()+"/"+entity.getName();
		return name;
	}
	
	/**
	 * @return the entity
	 */
	public GameObject getEntity() {
		return entity;
	}

}
