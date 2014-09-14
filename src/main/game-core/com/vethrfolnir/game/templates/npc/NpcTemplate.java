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
package com.vethrfolnir.game.templates.npc;

/**
 * @author Vlad
 *
 */
public class NpcTemplate {
	public int NpcId;
	public String Name;
	public int Level;
	
	public int HP;
	public int MP;
	public int MinDmg;
	public int MaxDmg;
	public int PDef;
	public int MagDef;
	public int Attack;
	public int Success;
	public int MoveAble;
	public int AtkType;
	public int AtkRange;
	public int VisRange;
	public int MovSpeed;
	public int AtkSpeed;
	public int RegenTime;
	public int Attrib;
	public int ItemRate;
	public int MoneyRate;
	public int MaxIS;
	public int RWind;
	public int RPois;
	public int RIce;
	public int RWtr;
	public int RFire;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NpcTemplate[id="+NpcId+" name="+Name+"]";
	}
}
