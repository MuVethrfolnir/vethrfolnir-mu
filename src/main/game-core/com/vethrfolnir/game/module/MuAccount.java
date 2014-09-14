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
package com.vethrfolnir.game.module;

import java.util.BitSet;
import java.util.List;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.templates.AccountCharacterInfo;

/**
 * @author Vlad
 *
 */
public final class MuAccount {

	private final MuClient client;
	private final String accountName;
	
	private final BitSet slots = new BitSet();

	private List<AccountCharacterInfo> characterList;
	
	/**
	 * @param muClient
	 * @param accountName
	 */
	public MuAccount(MuClient muClient, String accountName) {
		this.client = muClient;
		this.accountName = accountName;
	}

	public void enteredLobby() {
		if(characterList == null)
			setCharacterList(DatabaseAccess.AccountAccess().getLobbyCharacters(client.getAccount()));
	}

	public void leftLobby() {
		setCharacterList(null);
	}

	/**
	 * @param characterList the characterList to set
	 */
	public void setCharacterList(List<AccountCharacterInfo> characterList) {
		if(characterList == null && this.characterList != null)
			this.characterList.clear();

		this.characterList = characterList;
	}
	
	/**
	 * @return the characterList
	 */
	public List<AccountCharacterInfo> getCharacterList() {
		return characterList;
	}
	
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	
	/**
	 * @return the client
	 */
	public MuClient getClient() {
		return client;
	}
	
	/**
	 * @return the slots
	 */
	public BitSet getSlots() {
		return slots;
	}

	/**
	 * @return -1 if theres no slot available
	 */
	public int obtainSlot() {
		if(slots.cardinality() >= 6)
			return -1;

		int slot = slots.nextClearBit(0);
		slots.set(slot, true);
		return slot;
	}
	
	public void free(int slot) {
		if(slot == -1)
			return;

		slots.set(slot, false);
	}

	/**
	 * @param name
	 * @return
	 */
	public AccountCharacterInfo getLobbyCharacter(String name) {
		if(characterList == null)
			throw new RuntimeException("Invalid request! Client is not in the lobby to be getting a character[name="+name+"]! ");
		
		for (int i = 0; i < characterList.size(); i++) {
			AccountCharacterInfo c = characterList.get(i);
			if(c.name.equals(name))
				return c;
		}
		
		return null;
	}

	/**
	 * @param info
	 */
	public void addCharacter(AccountCharacterInfo info) {
		if(characterList == null)
			throw new RuntimeException("Invalid request! Client is not in the lobby to be creating a character[name="+info.name+"]! ");

		characterList.add(info);
	}
}
