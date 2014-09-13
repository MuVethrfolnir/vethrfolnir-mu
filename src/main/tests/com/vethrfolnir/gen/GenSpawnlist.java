/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.gen;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.NodeList;

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
			NodeList list = GenWorlds.getDocument(GenWorlds.class.getResourceAsStream("/worlds.xml"));
			ArrayList<Region> regions = GenWorlds.parseData(list);
			
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
				GenData.asArrayList(new FileInputStream(file), GenData.mp, SpawnTemplate.class);
			}

		}
	}
}
