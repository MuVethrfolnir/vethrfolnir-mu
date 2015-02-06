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
package com.vethrfolnir.game.templates.item;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * @author Constantin
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class ItemTemplate {
	public int uniqueId;
	public int index;
	public int id;
	public String Name;
	public int Slot;
	public int Skill;
	public int X;
	public int Y;
	public int Serial;
	public int Opt;
	public int Drop;
	public int Value;
	public int Lvl;
	public int DmgMin;
	public int DmgMax;
	public int Speed;
	public int Dur;
	public int MagDur;
	public int MagPow;
	public int ReqLvl;
	public int Def;
	public int Str;
	public int Agi;
	public int Ene;
	public int Vit;
	public int Com;
	public int Price;
	public int Type;
	public int Attr;
	public int DW;
	public int DK;
	public int ELF;
	public int MG;
	public int DL;
	public int SUM;
	public int MONK;
	public int R1, R2, R3, R4, R5, R6, R7;
	
	@JsonIgnore
	public ItemType getItemType() {
		return ItemType.values()[index];
	}
	
	@Override
	public String toString() {
		return "ItemTemplate[index="+index+" id="+id+" name="+Name+"]";
	}
}
