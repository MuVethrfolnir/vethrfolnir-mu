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

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;

import com.vethrfolnir.game.module.MuAccount;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.crypt.MuCryptUtils;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.game.templates.AccountInfo;

/**
 * @author Vlad
 *
 */
public class AccountDAO extends DAO {

	public AccountInfo getInfo(String accountName) {
		return enqueueAndWait((Connection con, Procedure<?> proc, Object... buff)-> {
			PreparedStatement st = con.prepareStatement("select password, accessLevel, lastAccess, creationDate from accounts where accountName = ?");
			st.setString(1, accountName);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				int p = 1;
				byte[] passData = MuCryptUtils.toByte(rs.getString(p++)); // password hex string
				int accessLevel = rs.getInt(p++);
				Date lastAccess = rs.getDate(p++);
				Date creationDate = rs.getDate(p++);
				
				return new AccountInfo(passData, accessLevel, lastAccess, creationDate);
			}
			
			return null;
		});
	}

	/**
	 * @param accountName
	 * @return
	 */
	public byte[] getPassword(String accountName) {
		return enqueueAndWait((con, prco, buff) -> {
			PreparedStatement st = con.prepareStatement("select password from accounts where accountName=?");
			st.setString(1, accountName);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				String name = rs.getString(1);
				return MuCryptUtils.toByte(name); // password hex string
			}

			return null;
		});
	}

	/**
	 * @param accountName
	 * @return
	 */
	public int getLobbyCharacterSize(String accountName) {
		return enqueueAndWait((con, proc, buff) -> {
			int count = 0;
			
			PreparedStatement st = con.prepareStatement("select count(*) from characters where accountName = ?");
			st.setString(1, accountName);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				count = rs.getInt(1);
			}

			return count;
		});
	}

	public ArrayList<AccountCharacterInfo> getLobbyCharacters(MuAccount account) {
		final ArrayList<AccountCharacterInfo> infos = new ArrayList<>();

		enqueueVoidAndWait((Connection con, Object... buff)-> {
			PreparedStatement st = con.prepareStatement("select charId, slot, name, level, accessLevel, classId from characters where accountName = ?");
			st.setString(1, account.getAccountName());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				AccountCharacterInfo info = new AccountCharacterInfo();
				int pointer = 1;
				info.charId = rs.getInt(pointer++);
				info.slot = rs.getInt(pointer++);
				info.name = rs.getString(pointer++);
				info.level = rs.getInt(pointer++);
				info.access = rs.getInt(pointer++);
				info.classId = rs.getInt(pointer++);

				// Slot used
				account.getSlots().set(info.slot, true);
				
				//TODO Get Inventory Info with charId
				
								
				infos.add(info);
			}
		});
		
		return infos;
	}
	
	public void createCharacter(String accountName, AccountCharacterInfo info, int mapId, int x, int y) {
		enqueueVoidAndWait((con, buff) -> {
			int pointer = 1;
			PreparedStatement st = con.prepareStatement("insert into characters(accountName, name, charId, slot, classId, mapId, x, y) values(?,?,?,?,?,?,?,?)");
			st.setString(pointer++, accountName);
			st.setString(pointer++, info.name);
			st.setInt(pointer++, info.charId);
			st.setInt(pointer++, info.slot);
			st.setInt(pointer++, info.classId);
			st.setInt(pointer++, mapId);
			st.setInt(pointer++, x);
			st.setInt(pointer++, y);
			st.execute();
		});
	}
	
	/**
	 * @param name
	 * @return
	 */
	public boolean isNameFree(String name) {
		return enqueueAndWait((con, proc, buff)-> {
			PreparedStatement st = con.prepareStatement("select name from characters where name=?");
			st.setString(1, name);
			return st.executeQuery().next();
		});
	}

	/**
	 * @param name
	 */
	public void deleteCharacter(String name) {
		enqueueVoidAndWait((con, buff)-> {
			PreparedStatement st = con.prepareStatement("delete from characters where name=?");
			st.setString(1, name);
			st.execute();
		});
	}


	public void updateAccount(MuAccount account) {
		
		enqueueVoid((con, buff) -> {
			PreparedStatement st = con.prepareStatement("update accounts set lastAccess=?, ip=? where accountName=?");
			st.setDate(1, new Date(System.currentTimeMillis()));
			st.setString(3, account.getClient().getHostAddress());
			st.setString(4, account.getAccountName());
			st.execute();
		});
	}
	
	/**
	 * @param accountName
	 * @param password
	 */
	public void createAccount(final String accountName, final String password, final MuClient client) {
		
		enqueueVoid((Connection con, Object... buff)-> {
			int pointer = 1;
			PreparedStatement st = con.prepareStatement("insert into accounts values(?,?,?,?,?,?)");
			st.setString(pointer++, accountName);
			MessageDigest md = MessageDigest.getInstance("md5");
			
			byte[] hash = md.digest(password.getBytes("UTF-8"));
			st.setString(pointer++, MuCryptUtils.toString(hash));
			st.setInt(pointer++, 0x00); // access level
			st.setDate(pointer++, new Date(System.currentTimeMillis())); // last access
			st.setDate(pointer++, new Date(System.currentTimeMillis())); // Creation Date
			st.setString(pointer++, client.getHostAddress());
			st.execute();
		});
	}

	/**
	 * @param client
	 */
	public void update(final MuClient client) {
		enqueueVoid((Connection con, Object... buff)-> {
			int pointer = 1;
			PreparedStatement st = con.prepareStatement("update accounts set lastAccess=?, ip = ? where accountName = ?");
			st.setDate(pointer++, new Date(System.currentTimeMillis()));
			st.setString(pointer++, client.getHostAddress());
			st.setString(pointer++, client.getAccount().getAccountName());
			st.execute();
		});
	}

}
