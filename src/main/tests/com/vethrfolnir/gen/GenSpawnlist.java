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
import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.game.templates.npc.SpawnTemplate;

/**
 * @author Vlad
 *
 */
public class GenSpawnlist {

	public static TIntObjectHashMap<String> supportedAreas = new TIntObjectHashMap<String>();
	static {
		try {
			ArrayList<Region> regions = GenData.asArrayList(new FileInputStream("./dist/GameServer/system/static/world-data.json"), GenData.getMapper(), Region.class);
			
			for (Region region : regions) {
				supportedAreas.put(region.getRegionId(), region.getRegionName());
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception  {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/MonsterSetBase.txt")))) {
			ArrayList<SpawnTemplate> templates = new ArrayList<SpawnTemplate>();

			int pointer = 1;
			int index = -1;

			
			while(br.ready()) {
				String line = br.readLine().replaceAll("\\t{2,9}", "	").trim();
				if(line.startsWith("//") || line.startsWith("#") || line.isEmpty())
					continue;
				
				if(index == -1 && line.replaceAll("[^\\p{L}\\p{N}]", "").trim().length() == 1) {
					index = Integer.parseInt(line.replaceAll("[^\\p{L}\\p{N}]", "").trim());
					//System.out.println("Starting Group! For index["+index+"]");
					continue;
				}
				else if (line.startsWith("end")) {
					//System.out.println("End of Group reached! For index["+index+"]");
					index = -1;
					continue;
				}
				
				String[] splits = line.trim().split("	");
				for (int i = 0; i < splits.length; i++) {
					splits[i] = splits[i].replaceAll("[^\\p{L}\\p{N}]", "");
				}

				switch (index) {
					case -1:
						System.out.println("Error missing index for line: "+line);
						break;
					case 2:// 2 : Standard Mob
						// Mob Map Rad X Y Str Name
					case 0: // 0 : NPC	
						// Mob Map Rad X Y Str Name
						
						SpawnTemplate template = new SpawnTemplate();
						int pnt = 0;
						template.NpcId = Integer.parseInt(splits[pnt++]);
						template.MapId = Integer.parseInt(splits[pnt++]);
						template.Radius = Integer.parseInt(splits[pnt++]);
						template.x = Integer.parseInt(splits[pnt++]);
						template.y = Integer.parseInt(splits[pnt++]);
						template.heading = Integer.parseInt(splits[pnt++]);
						String name = splits.length < 7 ? null : splits[pnt++];
						
						template.Attackable = index >= 2;
						templates.add(template);
						break;
					case 1: // 1 : Multiple Mobs
						//[Mob] [Map] [Distance] [StartX] [StartY] [EndX] [EndY] [Dir] [Total] [Description]
						pnt = 0;
						
						template = new SpawnTemplate();
						template.NpcId = Integer.parseInt(splits[pnt++]);
						template.MapId = Integer.parseInt(splits[pnt++]);
						template.Radius = Integer.parseInt(splits[pnt++]);
						template.StartX = Integer.parseInt(splits[pnt++]);
						template.StartY = Integer.parseInt(splits[pnt++]);
						template.x = Integer.parseInt(splits[pnt++]);
						template.y = Integer.parseInt(splits[pnt++]);
						template.heading = Integer.parseInt(splits[pnt++]);
						template.Count = Integer.parseInt(splits[pnt++]);
						name = splits.length < 10 ? null : splits[pnt++];
						templates.add(template);
						break;
					default:
						System.out.println("Skipping index["+index+"]");
						break;
				}
			}

			HashMap<String, ArrayList<SpawnTemplate>> mappedTemplates = new HashMap<>();

			int count = 0, totalCount = 0;
			for (int i = 0; i < templates.size(); i++) {
				SpawnTemplate template = templates.get(i);
				int mapId = template.MapId;
				
				String mapping = supportedAreas.get(mapId);
				
				if(mapping != null) {
					ArrayList<SpawnTemplate> map = mappedTemplates.get(mapping);
					
					if(map == null) {
						map = new ArrayList<>();
						mappedTemplates.put(mapping, map);
					}
					
					count++;
					count += template.Count;
					map.add(template);
				}
				
				totalCount++;
				totalCount += template.Count;
			}
			
			System.out.println("Templates: "+count+" skipped "+(totalCount));
			
			File dir = new File("./dist/GameServer/system/static/spawnlists/");
			dir.mkdirs();
			
			ObjectWriter writer = GenData.getWriter();
			
			for(Entry<String, ArrayList<SpawnTemplate>> e: mappedTemplates.entrySet()) {
				File file = new File(dir, (e.getKey().replace('/', '-'))+".json");
				System.out.println("Doing: "+file);
				file.createNewFile();
				
				writer.writeValue(new FileOutputStream(file), e.getValue());
			}

		}
	}
}
