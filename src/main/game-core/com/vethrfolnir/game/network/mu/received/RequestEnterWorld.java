/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.*;
import com.vethrfolnir.game.network.mu.MuClient.ClientStatus;
import com.vethrfolnir.game.network.mu.send.StateChange;
import com.vethrfolnir.game.network.mu.send.StatusInfo;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

/**
 * @author Vlad
 *
 */
public class RequestEnterWorld extends ReadPacket {

	@FetchIndex
	private ComponentIndex<PlayerState> state;
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.ReadPacket#read(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		MuClient client = as(context);
		String name = readS(buff, 10);

		AccountCharacterInfo info = client.getAccount().getLobbyCharacter(name);
		
		if(info == null)
			client.close();

		client.buildPlayer(info);
		client.setStatus(ClientStatus.InGame);

		PlayerState state = client.getEntity().get(this.state);
		client.sendPacket(MuPackets.EnterWorld);

		client.sendPacket(MuPackets.InventoryInfo);
		//client.sendPacket(new SkillListInfo());
		
		// Max HP / SD
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_HPSD, false);
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_HPSD, true);

		// Max MP / Stamina
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_MPST, false);
		client.sendPacket(MuPackets.StatusInfo, StatusInfo.STATUS_MPST, true);

		if(state.getAccessLevel() > 0)
			client.sendPacket(MuPackets.StateChange, StateChange.StateGM); // GM
	}

}
