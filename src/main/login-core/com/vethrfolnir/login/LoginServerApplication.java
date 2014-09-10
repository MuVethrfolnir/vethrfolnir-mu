/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.login.network.NetworkClientServer;
import com.vethrfolnir.login.network.NetworkGameServer;
import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.tools.Tools;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Initiate;
import corvus.corax.processing.annotation.Provide;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public class LoginServerApplication {

	private static final MuLogger log = MuLogger.getLogger(LoginServerApplication.class);
	
	private NetworkGameServer gameServer;
	private NetworkClientServer clientServer;
	
	@Initiate
	private void load() {
		Tools.printSection("Services");
		Corax.getInstance(GameNameService.class);
		Corax.getInstance(CorvusThreadPool.class);
		
		Tools.printSection("Networking");

		Corax.pDep(gameServer = new NetworkGameServer(),
				clientServer = new NetworkClientServer());

		Tools.printSection("Status");
		System.gc();

		gameServer.start();
		clientServer.start();
	}
	
	/**
	 * @return the clientServer
	 */
	@Provide
	public NetworkClientServer getClientServer() {
		return clientServer;
	}
	
	/**
	 * @return the gameServer
	 */
	@Provide
	public NetworkGameServer getGameServer() {
		return gameServer;
	}
}
