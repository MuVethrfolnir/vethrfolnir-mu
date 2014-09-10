/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.module.MuAccount;
import com.vethrfolnir.game.network.mu.crypt.MuEncoder;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.game.templates.PlayerTemplate;
import com.vethrfolnir.game.util.ListenerKeys;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 */
public final class MuClient extends NetworkClient {

	@SuppressWarnings("unused")
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
		
		EntityWorld world = Corax.getInstance(EntityWorld.class);
		entity = world.obtain(this);
	}

	@Initiate
	private void initiate() {
		// Prepare getting info from mysql
	}
	
	public void buildPlayer(AccountCharacterInfo info) {
		
		PlayerTemplate template = DatabaseAccess.PlayerAccess().loadPlayer(info.charId);
		entity.setName(template.name);
		
		entity.add(new Positioning(template.x, template.y, template.mapId));
		
		entity.add(new PlayerState(template));
		entity.add(new PlayerStats(template));
		entity.commit();
	}
	
	public void clean() {
		Corax.listen(ListenerKeys.ClientDisconnected, null, this);
		
		// Perform cleaning
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ClientStatus status) {
		ClientStatus old = this.status;
		this.status = status;

		if(this.status == ClientStatus.Authed && old != ClientStatus.Authed)
			account.enteredLobby();
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
		clean();
		entity.destroy();

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
		ByteBuf buff = channel().alloc().heapBuffer().order(ByteOrder.LITTLE_ENDIAN); // Why??

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
		String name = account == null? "I/O" : entity == null? account.getAccountName() : account.getAccountName()+"/"+entity.getName();
		return name;
	}
	
	/**
	 * @return the entity
	 */
	public GameObject getEntity() {
		return entity;
	}

}
