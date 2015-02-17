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

import java.util.ArrayList;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.entitys.components.player.PlayerState;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.send.SystemMessage.MessageType;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Constantin
 *
 */
public final class MuParty {
	public static int MaxPartyMembers = 5;
	private final ArrayList<GameObject> partyMembers = new ArrayList<GameObject>();
	private final GameObject leader;
	
	private static final MuLogger log = MuLogger.getLogger(MuParty.class);
	
	public MuParty(GameObject lead) {
		leader = lead;

		partyMembers.add(leader);
		PlayerState state = leader.get(PlayerMapping.PlayerState);
		state.setParty(this);
	}

	public ArrayList<GameObject> getPartyMembers() {
		return partyMembers;
	}
	
	public int size() {
		return partyMembers.size();
	}
	
	public boolean isPartyLeader(GameObject activeChar) {
		return activeChar == leader;
	}

	/**
	 * @param target
	 * @param answer
	 */
	public void invitePartyAnswer(GameObject target, boolean answer) {
		PlayerState targetState = target.get(PlayerMapping.PlayerState);
		if(targetState.getParty() != null) {
			leader.sendPacket(MuPackets.SystemMessage, target.getName()+" is already in a party.", MessageType.Normal);
			return;
		}
		
		log.info("Client["+leader+"] added "+target.getName()+" to his party.");
		
		partyMembers.add(target);
		targetState.setParty(this);
		
		updateUI(false);
		sendPacket(MuPackets.SystemMessage, "Veth: "+target.getName()+" has joined the party.", MessageType.Normal);
	}
	
	public GameObject getPartyMember(int partySlot) {
		return partyMembers.get(partySlot);
	}
	
	public void removePartyMember(GameObject target) {
		PlayerState targetState = target.get(PlayerMapping.PlayerState);
		
		if(isPartyLeader(target)) { // delete party
			dismatleParty();
			return;
		}

		targetState.setParty(null);
		updateUI(target, true);
		partyMembers.remove(target);
		
		if(size() <= 1) {
			dismatleParty();
			return;
		}

		sendPacket(MuPackets.SystemMessage, "Veth: "+target.getName()+" has left the party.", MessageType.Normal);
		updateUI(false);
	}
	
	public void dismatleParty() { 
		sendPacket(MuPackets.SystemMessage, "Veth: The party has been dismantled.", MessageType.Normal);
		updateUI(true);
		
		for(GameObject member : partyMembers) {
			PlayerState memberState = member.get(PlayerMapping.PlayerState);
			memberState.setParty(null);
		}
		
		partyMembers.clear();
		PlayerState leaderState = leader.get(PlayerMapping.PlayerState);
		leaderState.setParty(null);
	}
	
	public void sendPacket(WritePacket serverPacket, Object... params) {
		for (GameObject member : partyMembers) {
			member.sendPacket(serverPacket, params);
		}
	}

	public void updateUI(boolean dismantle) {
		sendPacket(MuPackets.PartyInfo, this, dismantle);
		sendPacket(MuPackets.ExPartyInfoBar, this);
	}

	public void updateUI(GameObject player, boolean dismantle) {
		player.sendPacket(MuPackets.PartyInfo, this, dismantle);
	}

	
}
