/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.login;

import com.vethrfolnir.game.network.login.send.ServerRegistration;

/**
 * @author Vlad
 *
 */
public class LoginPackets {
	/**
	 * Packet Type: Writable
	 * Server Registration packet on login
	 * @param None
	 */
	public static final ServerRegistration ServerAuthPacket = new ServerRegistration();

}
