/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.login.network.game;

import com.vethrfolnir.login.network.game.send.KillPacket;

/**
 * @author Vlad
 *
 */
public final class GamePackets {
	
	/**
	 * Packet Type: Writable
	 * Sends the kill packet along side a message
	 * @param KillType
	 */
	public static final KillPacket KillServer = new KillPacket();
}
