/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
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
