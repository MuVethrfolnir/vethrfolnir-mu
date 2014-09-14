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
package com.vethrfolnir.game.templates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * @author Vlad
 *
 * Node: A lot of unknowns here.. for me at least..
 */
@JsonInclude(Include.NON_DEFAULT)
public class SkillTemplate {

	public int SkillId;
	public String Name;
	public int ReqLvl;
	public int Dmg;
	public int Mana;
	public int Bp;
	public int Dis;
	public int Delay;
	public int Energy;
	public int Command;
	public int Attr;
	public int Type;
	public int UseType;
	public int Brand;
	public int KillCnt;
	public int Status1;
	public int Status2;
	public int Status3;
	public int DW;
	public int DK;
	public int ELF;
	public int MG;
	public int DL;
	public int SUM;
	public int MONK;
	public int Rank;
	public int Group;
	public int MasterP;
	public int AG;
	public int SD;
	public int Dur;
	public int Str;
	public int Dex;
	public int Icon;
	public int UseType2;
	public int Item;
	public boolean IsDamage;
}
