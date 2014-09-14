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

import java.io.*;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.vethrfolnir.game.templates.npc.NpcTemplate;

/**
 * @author Vlad
 *
 */
public class GenMonsters {

	public static void main(String[] args) throws Exception {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/Monster.txt")))) {
			ArrayList<NpcTemplate> templates = new ArrayList<>();
			while(br.ready()) {
				String line = br.readLine().replaceAll("\\t{2,9}", "	");
				
				if(line.isEmpty() || line.startsWith("//") || line.endsWith("end"))
					continue;
				
				String[] split = line.split("\\t");

				NpcTemplate template = new NpcTemplate();
				
				int ptr = 0;
				
				template.NpcId = Integer.parseInt(split[ptr++]); // No
				Integer.parseInt(split[ptr++]); // Rate
				
				template.Name = split[ptr++].replace("\"", ""); // Name
				template.Level = Integer.parseInt(split[ptr++]); //Lvl
				
				template.HP = Integer.parseInt(split[ptr++]);
				template.MP = Integer.parseInt(split[ptr++]);
				template.MinDmg = Integer.parseInt(split[ptr++]);
				template.MaxDmg = Integer.parseInt(split[ptr++]);
				template.PDef = Integer.parseInt(split[ptr++]);
				template.MagDef = Integer.parseInt(split[ptr++]);
				template.Attack = Integer.parseInt(split[ptr++]);
				template.Success = Integer.parseInt(split[ptr++]);
				template.MoveAble = Integer.parseInt(split[ptr++]);
				template.AtkType = Integer.parseInt(split[ptr++]);
				template.AtkRange = Integer.parseInt(split[ptr++]);
				template.VisRange = Integer.parseInt(split[ptr++]);
				template.MovSpeed = Integer.parseInt(split[ptr++]);
				template.AtkSpeed = Integer.parseInt(split[ptr++]);
				template.RegenTime = Integer.parseInt(split[ptr++]);
				template.Attrib = Integer.parseInt(split[ptr++]);
				template.ItemRate = Integer.parseInt(split[ptr++]);
				template.MoneyRate = Integer.parseInt(split[ptr++]);
				template.MaxIS = Integer.parseInt(split[ptr++]);
				template.RWind = Integer.parseInt(split[ptr++]);
				template.RPois = Integer.parseInt(split[ptr++]);
				template.RIce = Integer.parseInt(split[ptr++]);
				template.RWtr = Integer.parseInt(split[ptr++]);
				template.RFire = Integer.parseInt(split[ptr++]);
				templates.add(template);
			}
			
			ObjectWriter writer = GenData.getWriter();
			new File("./dist/GameServer/system/static/npc/").mkdirs();
			File file = new File("./dist/GameServer/system/static/npc/npc-data.json");
			file.createNewFile();
			
			writer.writeValue(new FileOutputStream(file), templates);
			
			// warm-up
			templates = GenData.asArrayList(new FileInputStream(file), GenData.getMapper(), NpcTemplate.class);
			
			long t1 = System.currentTimeMillis();
			templates = GenData.asArrayList(new FileInputStream(file), GenData.getMapper(), NpcTemplate.class);
			long end = System.currentTimeMillis();

			for (int i = 0; i < templates.size(); i++) {
				NpcTemplate t = templates.get(i);
				System.out.println(t);
			}
			
			System.out.println("Parsed: "+templates.size()+ " in "+(end - t1)+" ms - about 150 w/o worm up");
			
		}
	}
}
