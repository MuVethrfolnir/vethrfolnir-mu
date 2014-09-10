/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.templates;


public class AccountCharacterInfo {
	public int charId;
	public int slot;
	public String name;
	public int level;
	public int access;
	public int classId;
	public static int[] wearBytes = new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xF3, 0x00, 0x00, 0x00, 0xF8, 0x00, 0x00, 0xF0, 0xFF, 0xFF, 0xFF, 0x00, 0x00 };
	/**
	 * @return
	 */
	public static int[] getWearBytes() {
		return wearBytes;
	}
	
}