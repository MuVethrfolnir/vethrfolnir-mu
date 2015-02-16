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
package com.vethrfolnir.game.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.entitys.components.player.PlayerStats;
import com.vethrfolnir.game.templates.PlayerTemplate;
import com.vethrfolnir.tools.Tools;



/**
 * @author Vlad
 *
 */
public class PlayerDAO extends DAO {

	public PlayerTemplate loadPlayer(int charId) {
		return enqueueAndWait((con, proc, buff)-> {
			
			PreparedStatement st = con.prepareStatement("select * from characters where charId=?");
			st.setInt(1, charId);

			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				PlayerTemplate template = new PlayerTemplate();
				template.name = rs.getString("name");
				template.charId = charId;
				template.classId = rs.getInt("classId");
				template.level = rs.getInt("level");
				template.currentExperience = rs.getLong("currentExperience");
				template.slot = rs.getInt("slot");
				template.mapId = rs.getInt("mapId");
				template.x = rs.getInt("x");
				template.y = rs.getInt("y");
				template.guild = rs.getInt("guild");
				template.guildRank = rs.getInt("guildRank");
				template.strength = rs.getInt("strength");
				template.agility = rs.getInt("agility");
				template.vitality = rs.getInt("vitality");
				template.energy = rs.getInt("energy");
				template.command = rs.getInt("command");
				template.freePoints = rs.getInt("freePoints");
				template.masterPoints = rs.getInt("masterPoints");
				template.masterLevel = rs.getInt("masterLevel");
				template.expandedInventory = rs.getInt("expandedInventory");
				template.credits = rs.getInt("credits");
				template.zen = rs.getInt("zen");
				template.accessLevel = rs.getInt("accessLevel");;
				
				return template;
			}
			
			return null;
		});
	}

	/**
	 * @param entity
	 */
	public void savePlayer(GameObject entity) {
		enqueueVoidAndWait((con, buff) -> {
			PlayerState state = entity.get(PlayerState.class);
			PlayerStats stats = entity.get(PlayerStats.class);
			Positioning positioning = entity.get(Positioning.class);
			Inventory inv = entity.get(CreatureMapping.Inventory);
			
			int pointer = 1;
			PreparedStatement st = con.prepareStatement("update characters set level=?, classId=?, currentExperience=?, mapId=?, x=?, y=?, guild=?, guildRank=?, strength=?, agility=?, vitality=?, energy=?, command=?, freePoints=?, masterPoints=?, masterLevel=?, expandedInventory=?, credits=?, zen=?, accessLevel=?, wearSet=? where charid=?");
			st.setInt(pointer++, stats.getLevel());
			st.setInt(pointer++, state.getClassId().classId);
			st.setInt(pointer++, (int) stats.getCurrentExperience());
			st.setInt(pointer++, positioning.getMapId());
			st.setInt(pointer++, positioning.getX());
			st.setInt(pointer++, positioning.getY());
			st.setInt(pointer++, 0); // Guild
			st.setInt(pointer++, 0); // Guild Rank
			st.setInt(pointer++, stats.getStrength());
			st.setInt(pointer++, stats.getAgility());
			st.setInt(pointer++, stats.getVitality());
			st.setInt(pointer++, stats.getEnergy());
			st.setInt(pointer++, stats.getCommand());
			st.setInt(pointer++, stats.getFreePoints());
			st.setInt(pointer++, stats.getMasterPoints());
			st.setInt(pointer++, stats.getMasterLevel());
			st.setInt(pointer++, state.getInventoryExpanded());
			st.setInt(pointer++, 0); // Credits
			st.setInt(pointer++, state.getZen());
			st.setInt(pointer++, state.getAccessLevel());
			st.setBytes(pointer++, Tools.toBytes(inv.getWearBytes()));
			st.setInt(pointer++, state.getCharId());
			st.execute();
		});
	}
}
