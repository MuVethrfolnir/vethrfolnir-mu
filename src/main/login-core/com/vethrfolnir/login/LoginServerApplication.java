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
package com.vethrfolnir.login;

import com.vethrfolnir.login.network.NetworkClientServer;
import com.vethrfolnir.login.network.NetworkGameServer;
import com.vethrfolnir.login.services.GameNameService;
import com.vethrfolnir.services.threads.CorvusThreadPool;
import com.vethrfolnir.tools.Tools;

import corvus.corax.Corax;
import corvus.corax.inject.Inject;
import corvus.corax.provide.Provide;

/**
 * @author Vlad
 *
 */
public class LoginServerApplication {

	private NetworkGameServer gameServer;
	private NetworkClientServer clientServer;
	
	@Inject
	private void load() {
		Tools.printSection("Services");
		Corax.fetch(GameNameService.class);
		Corax.fetch(CorvusThreadPool.class);
		
		Tools.printSection("Networking");
		Corax.process(gameServer = new NetworkGameServer());
		Corax.process(clientServer = new NetworkClientServer());

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
