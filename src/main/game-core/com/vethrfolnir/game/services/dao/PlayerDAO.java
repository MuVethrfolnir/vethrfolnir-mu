/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vethrfolnir.game.templates.PlayerTemplate;

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
}
