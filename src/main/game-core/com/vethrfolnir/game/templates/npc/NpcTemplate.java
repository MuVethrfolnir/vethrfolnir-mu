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
