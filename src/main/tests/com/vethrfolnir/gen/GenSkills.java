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

import com.vethrfolnir.game.templates.SkillTemplate;

/**
 * @author Vlad
 *
 */
public class GenSkills {

	public static void main(String[] args) {
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/Skill.txt")))) {
			ArrayList<SkillTemplate> templates = new ArrayList<>();
			
			while(br.ready()) {
				String line = br.readLine().replaceAll("\\t{2,9}", "	").trim();
				
				if(line.isEmpty() || line.startsWith("//") || line.startsWith("end"))
					continue;
				
				String[] splits = line.split("\\t");
				
				for (int i = 0; i < splits.length; i++) {
					if(i == 1)
						continue;
					splits[i] = splits[i].replaceAll("[^\\p{L}\\p{N}]", "");
				}

				int pointer = 0;
				
				SkillTemplate template = new SkillTemplate();
				template.SkillId = Integer.parseInt(splits[pointer++]);
				template.Name = splits[pointer++].replace("\"", "");
				template.ReqLvl = Integer.parseInt(splits[pointer++]);
				template.Dmg = Integer.parseInt(splits[pointer++]);
				template.Mana = Integer.parseInt(splits[pointer++]);
				template.Bp = Integer.parseInt(splits[pointer++]);
				template.Dis = Integer.parseInt(splits[pointer++]);
				template.Delay = Integer.parseInt(splits[pointer++]);
				template.Energy = Integer.parseInt(splits[pointer++]);
				template.Command = Integer.parseInt(splits[pointer++]);
				template.Attr = Integer.parseInt(splits[pointer++]);
				template.Type = Integer.parseInt(splits[pointer++]);
				template.UseType = Integer.parseInt(splits[pointer++]);
				template.Brand = Integer.parseInt(splits[pointer++]);
				template.KillCnt = Integer.parseInt(splits[pointer++]);
				template.Status1 = Integer.parseInt(splits[pointer++]);
				template.Status2 = Integer.parseInt(splits[pointer++]);
				template.Status3 = Integer.parseInt(splits[pointer++]);
				template.DW = Integer.parseInt(splits[pointer++]);
				template.DK = Integer.parseInt(splits[pointer++]);
				template.ELF = Integer.parseInt(splits[pointer++]);
				template.MG = Integer.parseInt(splits[pointer++]);
				template.DL = Integer.parseInt(splits[pointer++]);
				template.SUM = Integer.parseInt(splits[pointer++]);
				template.MONK = Integer.parseInt(splits[pointer++]);
				template.Rank = Integer.parseInt(splits[pointer++]);
				template.Group = Integer.parseInt(splits[pointer++]);
				template.MasterP = Integer.parseInt(splits[pointer++]);
				template.AG = Integer.parseInt(splits[pointer++]);
				template.SD = Integer.parseInt(splits[pointer++]);
				template.Dur = Integer.parseInt(splits[pointer++]);
				template.Str = Integer.parseInt(splits[pointer++]);
				template.Dex = Integer.parseInt(splits[pointer++]);
				template.Icon = Integer.parseInt(splits[pointer++]);
				template.UseType2 = Integer.parseInt(splits[pointer++]);
				template.Item = Integer.parseInt(splits[pointer++]);
				template.IsDamage = Boolean.parseBoolean(splits[pointer++]);
				templates.add(template);
			}
			
			System.out.println("Loaded: "+templates.size());
			
			File dir = new File("./dist/GameServer/system/static/skills");
			dir.mkdirs();
			
			File file = new File(dir, "skills.json");

			GenData.getWriter().writeValue(new FileOutputStream(file), templates);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
