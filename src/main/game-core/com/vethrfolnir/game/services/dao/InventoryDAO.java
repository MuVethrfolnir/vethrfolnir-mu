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

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.entitys.components.player.Appearance;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.module.StaticData;
import com.vethrfolnir.game.module.item.MuItem;
import com.vethrfolnir.game.templates.item.ItemTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Disposable;

/**
 * @author Vlad
 *
 */
public class InventoryDAO extends DAO {

	private static final MuLogger log = MuLogger.getLogger(InventoryDAO.class);
	
	public void writeInventory(final GameObject e) {
		enqueueVoidAndWait(new VoidProcedure() {
			@Override
			public void perform(Connection con, Object... buff) throws Exception {
				Inventory inv = e.get(CreatureMapping.Inventory);
				Appearance app = e.get(PlayerMapping.Appearance);
				
				ArrayList<MuItem> items = new ArrayList<MuItem>();
				items.addAll(app.getPaperdolls().values());
				items.addAll(inv.getItems());
				
				for (int i = 0; i < buff.length; i++) {
					MuItem item = items.get(i);
					String sql = item.isNew() ? // TODO has to go after were done with DAO's 
						"update character_items set ownerId=?, itemId=?,durabilityCount=?,itemLevel=?,slot=?,skill=?,luck=?,option=?,execOption1=?,execOption2=?,execOption3=?,execOption4=?,execOption5=?,execOption6=?,option380=?,harmonyType=?,harmonyEnchant=?,socket1=?,socket2=?,socket3=?,socket4=?,socket5=? where objectId=?" :
						"INSERT INTO `character_items` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
					
					int pointer = 0;
					try(PreparedStatement st = con.prepareStatement(sql )) {
						st.setInt(pointer++, item.getOwnerId());
						st.setInt(pointer++, item.getUniqueId());
						st.setInt(pointer++, item.getDurabilityCount());
						st.setInt(pointer++, item.getItemLevel());
						st.setInt(pointer++, item.getSlot());
						st.setInt(pointer++, item.getSkill());
						st.setInt(pointer++, item.getLuck());
						st.setInt(pointer++, item.getAddOption());
						st.setInt(pointer++, item.getExcelentOption1());
						st.setInt(pointer++, item.getExcelentOption2());
						st.setInt(pointer++, item.getExcelentOption3());
						st.setInt(pointer++, item.getExcelentOption4());
						st.setInt(pointer++, item.getExcelentOption5());
						st.setInt(pointer++, item.getOption380());
						st.setInt(pointer++, item.getHarmonyType());
						st.setInt(pointer++, item.getHarmonyEnchant());
						st.setInt(pointer++, item.getSocketOption1());
						st.setInt(pointer++, item.getSocketOption2());
						st.setInt(pointer++, item.getSocketOption3());
						st.setInt(pointer++, item.getSocketOption4());
						st.setInt(pointer++, item.getSocketOption5());
						st.setInt(pointer++, item.getObjectId());
						st.execute();
					}
					catch (Exception e2) {
						log.log(Level.SEVERE, "Failed "+(item.isNew() ? "inserting" : "updating")+" item["+item.getUniqueId()+"] for owner["+e.getName()+"/"+e.get(PlayerMapping.PlayerState).getCharId()+"]");
					}
				}
				
				Disposable.dispose(items);
			}
		});
	}
	
	public ArrayList<MuItem> loadInventory(final int ownerId) {
		ArrayList<MuItem> list = new ArrayList<MuItem>();
		
		enqueueVoidAndWait(new VoidProcedure() {
			@Override
			public void perform(Connection con, Object... buff) throws Exception {
				PreparedStatement st = con.prepareStatement("select * from character_items where ownerId=?");
				st.setInt(1, ownerId);

				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					ItemTemplate template = StaticData.getItemTemplate(rs.getInt("itemId"));
				
					if(template == null) {
						log.warn("Skipping item with id["+rs.getInt("itemId")+"], template could not be found for player[id="+ownerId+"]!");
						continue;
					}
					
					MuItem item = new MuItem(rs.getInt("objectId"), template);
					item.setDurabilityCount(rs.getInt("durabilityCount"));
					item.setItemLevel(rs.getInt("itemLevel"));
					item.setSlot(rs.getInt("slot"));
					item.setSkill(rs.getInt("skill"));
					item.setLuck(rs.getInt("luck"));
					item.setAddOption(rs.getInt("option"));
					item.setExcelentOption1(rs.getInt("execOption1"));
					item.setExcelentOption2(rs.getInt("execOption2"));
					item.setExcelentOption3(rs.getInt("execOption3"));
					item.setExcelentOption4(rs.getInt("execOption4"));
					item.setExcelentOption5(rs.getInt("execOption5"));
					item.setExcelentOption6(rs.getInt("execOption6"));
					
					if(rs.getInt("option380") > 0)
						item.setOption380();

					item.setHarmonyType(rs.getInt("harmonyType"));
					item.setHarmonyEnchant(rs.getInt("harmonyEnchant"));
					item.setSocketOption1(rs.getInt("socket1"));
					item.setSocketOption2(rs.getInt("socket2"));
					item.setSocketOption3(rs.getInt("socket3"));
					item.setSocketOption4(rs.getInt("socket4"));
					item.setSocketOption5(rs.getInt("socket5"));
					item.setNew(false);
					
					list.add(item);
				}
			}
		});
		
		return list;
	}
	
	public void deleteInventory(int ownerId) {
		enqueueVoidAndWait(new VoidProcedure() {
			@Override
			public void perform(Connection con, Object... buff) throws Exception {
				PreparedStatement st = con.prepareStatement("delete from character_items where ownerId=?");
				st.setInt(1, ownerId);
				st.execute();
			}
		});
	}
}
