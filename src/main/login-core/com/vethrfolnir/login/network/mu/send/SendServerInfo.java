/*
 This application is part of corvus engine, and its strictly bounded to its owner.
 Using corvus engine unauthorized is strictly forbidden.
*/
package com.vethrfolnir.login.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.login.services.GameNameService.GameServer;
import com.vethrfolnir.login.templates.MuGameServerTemplate;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Seth
 */
public class SendServerInfo extends WritePacket {
	
	public static final SendServerInfo StaticPacket = new SendServerInfo();
	private static final MuLogger _log = MuLogger.getLogger(SendServerInfo.class);
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(com.vethrfolnir.network.NetworkClient, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		GameNameService gsd = as(params[0]);
		int _serverid = as(params[1]);
		
		MuGameServerTemplate serverinfo = gsd.getServer(_serverid);
		
		if(serverinfo == null)
		{
			_log.warn(getClass().getSimpleName()+": got server info null .");
			return;
		}
		
		GameServer gsi = gsd.getLiveServers().get(serverinfo).get(_serverid);
		
		if(gsi == null)
		{
			_log.warn(getClass().getSimpleName()+": got game server info null .");
			return;
		}
		
		writeC(buff, 0xC1);
		writeC(buff, 0x16);
		writeC(buff, 0xF4);
		writeC(buff, 0x03);
		
		try
		{
			byte[] gsip = gsi.getHost().getBytes("US-ASCII");
			writeArray(buff, 16, gsip); // de aici e problema
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		
		writeSh(buff, gsi.getPort());
	}

}
