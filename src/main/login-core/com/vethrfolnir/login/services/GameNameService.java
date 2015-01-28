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
package com.vethrfolnir.login.services;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.login.network.game.GamePackets;
import com.vethrfolnir.login.network.game.send.KillPacket.KillType;
import com.vethrfolnir.login.templates.MuGameServerTemplate;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.services.DataMappingService;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public class GameNameService {
	
	private static final MuLogger _log = MuLogger.getLogger(GameNameService.class);
	
	private final HashMap<MuGameServerTemplate, HashMap<Integer, GameServer>> serverPool = new HashMap<>(10);
	
	private AtomicInteger liveServers = new AtomicInteger();
	
	@Inject
	private void load(DataMappingService service) {

		try {
			ArrayList<MuGameServerTemplate> data = service.asArrayList(MuGameServerTemplate.class, "server-names.json");
			
			for (MuGameServerTemplate t : data) {
				serverPool.put(t, new HashMap<Integer, GameServer>());
			}
			
			data.clear();
		}
		catch (Exception e) {
			_log.fatal("Unable to load name server data!", e);
		}
		
		_log.info("Loaded "+serverPool.size()+" game server name(s)!");
	}
	
	public void registerNewServer(GameServer gns, String password) {
		String serverPws = Corax.config().getProperty("GameServer.Password", "root");

		boolean allow = serverPws.equals(password);
		
		if(allow)
		{
			MuGameServerTemplate serv = gns.getTemplate();
			
			if(serv != null)
			{
				if(serverPool.get(gns.getTemplate()).containsKey(gns.getServerId()))
				{
					gns.sendPacket(GamePackets.KillServer, KillType.ID_TAKEN);	
					_log.info("Server with Host: "+gns.channel()+" tryed to connect on the same id with another one id: "+gns.getServerId()+".");
					return;
				}
				else if (gns.isAcceptAnyId()) {
					int id = getFreeId(serv);

					gns.setServerId(id);
					
					if(id <= -1) {
						_log.warn("Failed finding available id for server with Host: "+gns.getHost());
						gns.sendPacket(GamePackets.KillServer, KillType.NO_AVIALIABLE_ID);
						return;
					}
				}

				serverPool.get(serv).put(gns.getServerId(), gns);
				_log.info("Server with Host: "+gns.getHost()+" connected as "+serv.getName()+" id: "+gns.getServerId()+".");
				liveServers.incrementAndGet();
			}
			else
			{
				_log.warn("Server with Host: "+gns.getHost()+" tryed to connect with a invalid server id: "+gns.getServerId());
				gns.sendPacket(GamePackets.KillServer, KillType.INCORRECT_ID);
			}
		}
		else
		{
			gns.sendPacket(GamePackets.KillServer, KillType.INCORRECT_PASSWORD);
		}
	}

	
	public int liveServerSize() {
		return liveServers.get();
	}

	private int getFreeId(MuGameServerTemplate template) {
		
		for (int i = template.getX(); i < template.getY(); i++) {
			if(!serverPool.get(template).containsKey(i)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public MuGameServerTemplate getServer(int id)
	{
		for(MuGameServerTemplate serv : serverPool.keySet())
		{
			if(id >= serv.getX() && id <= serv.getY())
				return serv;
		}
		
		return null;
	}
	
	public int getServerIndex(int id)
	{
		for(MuGameServerTemplate serv : serverPool.keySet())
		{
			if(id >= serv.getX() && id <= serv.getY())
			{
				return (serv.getX()) / 2;
			}
		}
		return 0;
	}
	
	public void removeServer(GameServer info)
	{
		liveServers.decrementAndGet();
		serverPool.get(info.getTemplate()).remove(info.getServerId());
		_log.info("Removed server "+info.getTemplate().getName()+" with id "+info.getServerId());
	}

	public GameServer create(ChannelHandlerContext context, int serverId, String host, int port) {
		GameServer gs = new GameServer(context, getServer(serverId), host, port);
		gs.setServerId(serverId);
		return gs;
	}
	
	public final class GameServer extends NetworkClient {
		private final String host;
		private final int port;

		private final ChannelHandlerContext context;
		private final MuGameServerTemplate template;

		private boolean acceptAnyId;
		private int serverId;
		private int onlinePlayers;
		private int cap;
		
		/**
		 * @param context
		 */
		public GameServer(ChannelHandlerContext context, MuGameServerTemplate template, String host, int port) {
			super(context.channel());
			this.context = context;
			this.host = host;
			this.port = port;
			this.template = template;
		}
		
		/**
		 * Should only be called by netty's handler!
		 */
		public void remove() {
			GameNameService.this.serverPool.get(template).remove(serverId);
			_log.info("Game Server with host["+host+"/"+port+"] has been removed.");
		}
		
		/**
		 * @return the template
		 */
		public MuGameServerTemplate getTemplate() {
			return template;
		}
		
		/**
		 * @param serverId the serverId to set
		 */
		public void setServerId(int serverId) {
			this.serverId = serverId;
		}
		/**
		 * @return the serverId
		 */
		public int getServerId() {
			return serverId;
		}
		
		/**
		 * @return the context
		 */
		public Channel getChannel() {
			return context.channel();
		}
		
		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}
		
		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}
		
		/**
		 * @return the acceptAnyId
		 */
		public boolean isAcceptAnyId() {
			return acceptAnyId;
		}
		
		/**
		 * @param acceptAnyId the acceptAnyId to set
		 */
		public void setAcceptAnyId(boolean acceptAnyId) {
			this.acceptAnyId = acceptAnyId;
		}
		
		/**
		 * @return the onlinePlayers
		 */
		public int getOnlinePlayers() {
			return onlinePlayers;
		}
		
		/**
		 * @param onlinePlayers the onlinePlayers to set
		 */
		public void setOnlinePlayers(int onlinePlayers) {
			this.onlinePlayers = onlinePlayers;
		}
		
		/**
		 * @return the cap
		 */
		public int getCap() {
			return cap;
		}
		
		/**
		 * @param cap the cap to set
		 */
		public void setCap(int cap) {
			this.cap = cap;
		}
		
		public void sendPacket(WritePacket packet, Object... params) {
			ByteBuf buff = context.alloc().buffer();
			packet.write(this, buff, params);
			packet.markLength(buff);
			context.writeAndFlush(buff);
		}
	}

	public HashMap<MuGameServerTemplate, HashMap<Integer, GameServer>> getLiveServers() {
		return serverPool;
	}

	/**
	 * TODO: Improve this
	 * @param channel
	 * @return
	 */
	public GameServer getServer(Channel channel) {
		for(HashMap<Integer, GameServer> ss : getLiveServers().values()) {
			for (GameServer s : ss.values()) {
				if(s.channel() == channel)
					return s;
			}
		}
		return null;
	}
}
