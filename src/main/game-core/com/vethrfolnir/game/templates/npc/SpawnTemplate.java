/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.templates.npc;

/**
 * @author Vlad
 *
 */
public class SpawnTemplate {

	public int NpcId;
	public int MapId;
	public int Radius;
	public int x;
	public int y;
	public int heading;

	public boolean Attackable;

	// Spawn Groups
	public int StartX = -1;
	public int StartY = -1;
	public int Count;
}
