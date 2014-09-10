/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.staticdata;

/**
 * @author Vlad
 *
 */
public enum ClassId {
	Dark_Wizzard(0),
	Sould_Master(1),
	Grand_Master(2),
	
	Dark_Knight(16),
	Blade_Knight(17),
	Blade_Master(18),
	
	Elf(32),
	Muse_Elf(33),
	High_Elf(34),
	
	Magic_Gladiator(48, 7),
	Duel_Master(49, 7),
	
	Dark_Lord(64, 7),
	Lord_Emperor(65, 7),
	
	Summoner(80),
	Bloody_Summoner(81),
	Dimensional_Master(82),
	
	Rage_Fighter(96, 7),
	Fist_Master(97, 7);

	public final int classId;
	public final int levelPoints;
	
	private ClassId(int classId) {
		this.classId = classId;
		this.levelPoints = 5;
	}
	
	private ClassId(int classId, int levelPoints) {
		this.classId = classId;
		this.levelPoints = levelPoints;
	}
	
	public static boolean isValid(int classId) {
		return getClass(classId) != null;
	}

	/**
	 * @param classId
	 * @return
	 */
	public static ClassId getClass(int classId) {
		ClassId[] arr = ClassId.values();
		for (int i = 0; i < arr.length; i++) {
			ClassId clsId = arr[i];
			if(clsId.classId == classId)
				return clsId;
		}
		return null;
	}
}
