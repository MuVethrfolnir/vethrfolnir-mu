/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.config;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Config;

/**
 * @author Vlad
 *
 */
public class PlayerConfig {

	@Config(key = "Player.FreeTeleport", subscribe = true, value = "false")
	public static boolean TeleportFree = false;

	@Config(key = "Player.TeleportNoRestriction", subscribe = true, value = "false")
	public static boolean TeleportNoRestriction = false;

	static {
		Corax.pDep(new PlayerConfig());
	}
}
