/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.received;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.config.PlayerConfig;
import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.game.entitys.annotation.FetchIndex;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.module.StaticData;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.send.SystemMessage.MessageType;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.ReadPacket;

import corvus.corax.Corax;


/**
 * @author Vlad
 *
 */
public class RequestSay extends ReadPacket {

	private final boolean isPrivate;

	public enum ChannelType
	{
		Normal('n'),
		Whisper('n'),
		Alliance('n'),
		Party('~'),
		Guild('@'),
		Announce('!');

		public final char type;
		
		ChannelType(char type) {
			this.type = type;
		}
	}

	@FetchIndex
	private ComponentIndex<PlayerStats> stats;

	@FetchIndex
	private ComponentIndex<PlayerState> state;

	@FetchIndex
	private ComponentIndex<Positioning> positioning;

	private final EntityWorld entityWorld;
	
	public RequestSay(boolean isPrivate) {
		this.isPrivate = isPrivate;
		entityWorld = Corax.getInstance(EntityWorld.class);
	}
	
	@Override
	public void read(NetworkClient context, ByteBuf buff, Object... params) {
		String from = readS(buff, 10).trim();
		String message = readS(buff, buff.readableBytes());

		if(message == null || message.isEmpty())
			return;
		
		boolean isNormal = Character.isLetter(message.charAt(0));
		MuClient client = (MuClient) context;
		
		if(isPrivate) {
			
			GameObject entity = entityWorld.findClient(from, true);
			
			if(entity != null)
				client.sendPacket(MuPackets.PlayerSay, from, message, true);
			else
				client.sendPacket(MuPackets.SystemMessage, "No player by that name is online.", MessageType.Normal);

			return;
		}
		
		if(message.startsWith("/move")) {
			String map = message.split(" ")[1];
			Region region = StaticData.getRegionData().getRegion(map);
			PlayerStats stats = client.getEntity().get(this.stats);
			if(region != null)
			{
				if(region.getMoveLevel() != -1 && (PlayerConfig.TeleportNoRestriction || (region.getMoveLevel() <= stats.getLevel()))) {
					region.transfer(client.getEntity());
					return;
				}
				else {
					if(region.getMoveLevel() != -1)
						client.sendPacket(MuPackets.SystemMessage, "You need to be at least level "+region.getMoveLevel()+" to move to "+region.getRegionName()+"!", MessageType.Normal);
					else {
						client.sendPacket(MuPackets.SystemMessage, "What are you doing?", MessageType.Normal);
					}
				}
			}
			
			return;
		}

		if (message.startsWith("//")) {
			PlayerState state = client.getEntity().get(this.state);
			
			if(state.getAccessLevel() > 0)
				client.sendPacket(MuPackets.SystemMessage, "What are you doing?", MessageType.Normal);
			
			return;
		}
		
		if(!isNormal)
		{
			ChannelType type = getChannelType(message.charAt(0));

			if(type == null)
				return;
			
			// TODO Implement Chat types!
			switch (type) {
				case Party:
					return;
				case Alliance:
					return;
				case Guild:
					return;
				default:
					break; 
			}
		}
		
		Positioning positioning = client.getEntity().get(this.positioning);
		
		client.sendPacket(MuPackets.PlayerSay, from, message);
		positioning.getCurrentRegion().broadcastToKnown(client.getEntity(), MuPackets.PlayerSay, from, message);
	}

	/**
	 * @param charAt
	 * @return
	 */
	private ChannelType getChannelType(char charAt) {

		for(int i = 0; i < ChannelType.values().length; i++)
		{
			ChannelType ct = ChannelType.values()[i];
			char ch = ct.type;

			if(charAt == ch)
				return ct;
		}
		
		return null;
	}
}
