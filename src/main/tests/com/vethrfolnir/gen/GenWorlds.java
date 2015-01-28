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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.vethrfolnir.game.staticdata.world.Region;

import corvus.corax.util.Tools;

/**
 * @author Vlad
 *
 */
public class GenWorlds {
	
	public static void main(String[] args) throws Exception {
		NodeList data = getDocument(GenWorlds.class.getResourceAsStream("/worlds.xml"));
		ArrayList<Region> regions = parseData(data);
		
		ObjectWriter writer = GenData.getWriter();
		writer.writeValue(new FileOutputStream("./dist/GameServer/system/static/world-data.json"), regions);
		
		System.out.println("Regions: "+regions);
	}

	public static NodeList getDocument(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(is).getChildNodes();
	}
	
	/**
	 * @param nodes
	 * @return
	 */
	public static ArrayList<Region> parseData(NodeList nodes) throws Exception {
		ArrayList<Region> regions = new ArrayList<>();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			
			if(node.getNodeName().startsWith("#"))
				continue;
			
			if(!node.getNodeName().equals("map")) {
				regions.addAll(parseData(node.getChildNodes()));
				continue;
			}
			//<map id="0" name="Lorencia" allowMove="true" x="141" y="133" moveLevel="10" geodataFile = "Terrain1.att"/>
			NamedNodeMap attributes = node.getAttributes();
			int id = getValue(attributes, "id", 0);
			String name = getValue(attributes, "name", "Unk");
			int x = getValue(attributes, "x", 0);
			int y = getValue(attributes, "y", 0);
			int moveLevel = getValue(attributes, "moveLevel", -1);
			String geodataFile = getValue(attributes, "geodataFile", null);

			Region region = new Region(id, name, geodataFile, moveLevel, x, y);
			regions.add(region);
		}
		
		return regions;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getValue(NamedNodeMap attributes, String name, T def) {
		Node obj = attributes.getNamedItem(name);
		
		if(obj != null && obj.getNodeValue() != null) {
			return (T) Tools.parsePrimitiveTypes(def == null ? String.class : def.getClass(), obj.getNodeValue());
		}
		
		return def;
	}

}
