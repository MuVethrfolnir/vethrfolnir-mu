/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
