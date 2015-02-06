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
package com.vethrfolnir.gen;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;
import java.util.ArrayList;

import com.vethrfolnir.game.templates.item.ItemTemplate;

/**
 * @author Constantin
 *
 */
public class GenItems {
	
	public static void main(String[] args) throws Exception {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/Item.txt")))) {
			TIntObjectHashMap<ArrayList<ItemTemplate>> templates = new TIntObjectHashMap<ArrayList<ItemTemplate>>();
			
			int prevIndex = 0;
			while(br.ready()) {
				String line = br.readLine().replaceAll("\\t{2,9}", "	").trim();
				
				if(line.isEmpty() || line.startsWith("//") || line.startsWith("end"))
					continue;
				
				String[] splits = line.split("\\t");

				for (int i = 0; i < splits.length; i++) {
					splits[i] = splits[i].replace("\"", "");
				}

				boolean hasMore = splits.length > 1;
				
				if(hasMore) {
					ArrayList<ItemTemplate> templaties = templates.get(prevIndex);
					
					if(templaties == null)
						templates.put(prevIndex, templaties= new ArrayList<ItemTemplate>());
					
					int pointer = 0;

					ItemTemplate template = new ItemTemplate();
					if(prevIndex < 6) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Lvl = Integer.parseInt(splits[pointer++]);
						template.DmgMin = Integer.parseInt(splits[pointer++]);
						template.DmgMax = Integer.parseInt(splits[pointer++]);
						template.Speed = Integer.parseInt(splits[pointer++]);
						template.Dur = Integer.parseInt(splits[pointer++]);
						template.MagDur = Integer.parseInt(splits[pointer++]);
						template.MagPow = Integer.parseInt(splits[pointer++]);
						template.ReqLvl = Integer.parseInt(splits[pointer++]);
						template.Str = Integer.parseInt(splits[pointer++]);
						template.Agi = Integer.parseInt(splits[pointer++]);
						template.Ene = Integer.parseInt(splits[pointer++]);
						template.Vit = Integer.parseInt(splits[pointer++]);
						template.Com = Integer.parseInt(splits[pointer++]);
						template.Type = Integer.parseInt(splits[pointer++]);
						template.DW = Integer.parseInt(splits[pointer++]);
						template.DK = Integer.parseInt(splits[pointer++]);
						template.ELF = Integer.parseInt(splits[pointer++]);
						template.MG = Integer.parseInt(splits[pointer++]);
						template.DL = Integer.parseInt(splits[pointer++]);
						template.SUM = Integer.parseInt(splits[pointer++]);
						template.MONK = Integer.parseInt(splits[pointer++]);
					}
					
					if(prevIndex > 5 && prevIndex < 12) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Lvl = Integer.parseInt(splits[pointer++]);
						template.Def = Integer.parseInt(splits[pointer++]);
						template.Speed = Integer.parseInt(splits[pointer++]);
						template.Dur = Integer.parseInt(splits[pointer++]);
						template.ReqLvl = Integer.parseInt(splits[pointer++]);
						template.Str = Integer.parseInt(splits[pointer++]);
						template.Agi = Integer.parseInt(splits[pointer++]);
						template.Ene = Integer.parseInt(splits[pointer++]);
						template.Vit = Integer.parseInt(splits[pointer++]);
						template.Com = Integer.parseInt(splits[pointer++]);
						template.Attr = Integer.parseInt(splits[pointer++]);
						template.DW = Integer.parseInt(splits[pointer++]);
						template.DK = Integer.parseInt(splits[pointer++]);
						template.ELF = Integer.parseInt(splits[pointer++]);
						template.MG = Integer.parseInt(splits[pointer++]);
						template.DL = Integer.parseInt(splits[pointer++]);
						template.SUM = Integer.parseInt(splits[pointer++]);
						template.MONK = Integer.parseInt(splits[pointer++]);
					}
					
					if(prevIndex == 12) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Lvl = Integer.parseInt(splits[pointer++]);
						template.Def = Integer.parseInt(splits[pointer++]);
						template.Dur = Integer.parseInt(splits[pointer++]);
						template.ReqLvl = Integer.parseInt(splits[pointer++]);
						template.Ene = Integer.parseInt(splits[pointer++]);
						template.Str = Integer.parseInt(splits[pointer++]);
						template.Agi = Integer.parseInt(splits[pointer++]);
						template.Com = Integer.parseInt(splits[pointer++]);
						template.Price = Integer.parseInt(splits[pointer++]);
						template.DW = Integer.parseInt(splits[pointer++]);
						template.DK = Integer.parseInt(splits[pointer++]);
						template.ELF = Integer.parseInt(splits[pointer++]);
						template.MG = Integer.parseInt(splits[pointer++]);
						template.DL = Integer.parseInt(splits[pointer++]);
						template.SUM = Integer.parseInt(splits[pointer++]);
						template.MONK = Integer.parseInt(splits[pointer++]);
					}
					
					if(prevIndex == 13) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Lvl = Integer.parseInt(splits[pointer++]);
						template.Dur = Integer.parseInt(splits[pointer++]);
						template.R1 = Integer.parseInt(splits[pointer++]);
						template.R2 = Integer.parseInt(splits[pointer++]);
						template.R3 = Integer.parseInt(splits[pointer++]);
						template.R4 = Integer.parseInt(splits[pointer++]);
						template.R5 = Integer.parseInt(splits[pointer++]);
						template.R6 = Integer.parseInt(splits[pointer++]);
						template.R7 = Integer.parseInt(splits[pointer++]);
						template.Attr = Integer.parseInt(splits[pointer++]);
						template.DW = Integer.parseInt(splits[pointer++]);
						template.DK = Integer.parseInt(splits[pointer++]);
						template.ELF = Integer.parseInt(splits[pointer++]);
						template.MG = Integer.parseInt(splits[pointer++]);
						template.DL = Integer.parseInt(splits[pointer++]);
						template.SUM = Integer.parseInt(splits[pointer++]);
						template.MONK = Integer.parseInt(splits[pointer++]);
					}
					

					
					if(prevIndex == 14) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Value = Integer.parseInt(splits[pointer++]);
						template.Lvl = Integer.parseInt(splits[pointer++]);
					}
					

					
					if(prevIndex == 15) {
						template.index = prevIndex;
						template.id = Integer.parseInt(splits[pointer++]);
						template.Slot = Integer.parseInt(splits[pointer++]);
						template.Skill = Integer.parseInt(splits[pointer++]);
						template.X = Integer.parseInt(splits[pointer++]);
						template.Y = Integer.parseInt(splits[pointer++]);
						template.Serial = Integer.parseInt(splits[pointer++]);
						template.Opt = Integer.parseInt(splits[pointer++]);
						template.Drop = Integer.parseInt(splits[pointer++]);
						template.Name = splits[pointer++];
						template.Lvl = Integer.parseInt(splits[pointer++]);
						template.ReqLvl = Integer.parseInt(splits[pointer++]);
						template.Ene = Integer.parseInt(splits[pointer++]);
						template.Price = Integer.parseInt(splits[pointer++]);
						template.DW = Integer.parseInt(splits[pointer++]);
						template.DK = Integer.parseInt(splits[pointer++]);
						template.ELF = Integer.parseInt(splits[pointer++]);
						template.MG = Integer.parseInt(splits[pointer++]);
						template.DL = Integer.parseInt(splits[pointer++]);
						template.SUM = Integer.parseInt(splits[pointer++]);
						template.MONK = Integer.parseInt(splits[pointer++]);
					}
					
					System.out.println(template.toString());
					
					templaties.add(template);
				}
				else {
					prevIndex = Integer.parseInt(splits[0]);
				}
			}
			
			ArrayList<ItemTemplate> mainTemplates = new ArrayList<ItemTemplate>();
			for (ArrayList<ItemTemplate> temps : templates.valueCollection()) {
				mainTemplates.addAll(temps);
			}
			
			//round(result / 512) = item index
		    //result - itemIndex * 512 = item id
			for (int i = 0; i < mainTemplates.size(); i++) {
				ItemTemplate temp = mainTemplates.get(i);
				temp.uniqueId = temp.index * 512 + temp.id;
			}
			
			System.out.println("Loaded: "+mainTemplates.size());
			
			File dir = new File("./dist/GameServer/system/static/items");
			dir.mkdirs();
			
			File file = new File(dir, "items.json");

			GenData.getWriter().writeValue(new FileOutputStream(file), mainTemplates);
			
			// do what ever with mainTemplates
		}
	}
	
}
