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
package com.vethrfolnir.game.network.mu;

import com.vethrfolnir.game.network.mu.send.*;
import com.vethrfolnir.game.network.mu.send.SystemMessage.MessageType;

/**
 * @author Vlad
 *
 */
public class MuPackets {
	
	/**
	 * Sends the HelloClient packet, dose not have any parameters.
	 */
	public static final HelloClient HelloClient = new HelloClient();
	
	/**
	 * Sends the result of the Auth attempt
	 * @param authResult[AuthResult:0]
	 */
	public static final ExClientAuthAnswer ExAuthAnswer = new ExClientAuthAnswer();
	
	/**
	 * Sends the Character list packet, dose not have any parameters.
	 */
	public static final LobbyCharacterList EnterLobby = new LobbyCharacterList();
	
	/**
	 * Sends the boolean that lets the client know that Summoners are available for creation 
	 */
	public static final AllowSummonerCreation AllowSummonerCreation = new AllowSummonerCreation(); 

	/**
	 * Sends the answer that the character on the slot has been created or not XD<br>
	 * Requires: A boolean and a AccountCharacterInfo/null
	 */
	public static final LobbyCreateCharacter CharacterCreateAnswer = new LobbyCreateCharacter();
	
	/**
	 * Sends the answer that the character was deleted or not XD<br>
	 * Requires: A boolean, was it deleted or not?
	 */
	public static final LobbyDeleteCharacter CharacterDeleteAnswer = new LobbyDeleteCharacter();
	
	/**
	 * Sends the answer that the character was selected, prepare to enter the fray!
	 * Requires: A boolean and a AccountCharacterInfo
	 */
	public static final LobbyCharacterSelected CharacterSelectedAnswer = new LobbyCharacterSelected();
	
	/**
	 * Sends the enter world packet
	 */
	public static final EnterWorld EnterWorld = new EnterWorld();
	
	/**
	 * Sends the logout packet<br>
	 * Requires: An integer, specifying the logout type
	 */
	public static final Logout Logout = new Logout();
	
	/**
	 * Sends the level up packet, giving the player points and the animation<br>
	 * Requires: The PlayerStats component
	 */
	public static final UserLevelUp LevelUp = new UserLevelUp();
	
	/**
	 * Sends the full inventory packet
	 */
	public static final InventoryInfo InventoryInfo = new InventoryInfo();
	
	/**
	 * Send the status info of hp/sd and mana/whatever
	 * Requires StatusInfo.TYPE and a boolean for current hp or maxhp
	 */
	public static final StatusInfo StatusInfo = new StatusInfo();

	/**
	 * Send the state change packet, for an entity, it includes active effects
	 * Requires: Effect Id
	 */
	public static final StateChange StateChange = new StateChange();
	
	/**
	 * Moves the object
	 * Requires: GameObject Entity
	 */
	public static final MoveObject MoveObject = new MoveObject();

	/**
	 * Deletes the object
	 * Requires: GameObject Entity
	 */
	public static final DeleteObject DeleteObject = new DeleteObject();

	/**
	 * Sends the Player information packet, that can update the players apparel and visually appear for players around him
	 * Requires: boolean toSelf? and if its not to self it requires the entity in question
	 */
	public static final PlayerInfo PlayerInfo = new PlayerInfo();
	
	/**
	 * Sends the NpcInfo packet to a client
	 * Requires: GameObject entity.
	 */
	public static final NpcInfo NpcInfo = new NpcInfo();
	
	/**
	 * Sends the damage info to the client<br>
	 * Requires: GameObject target, int dmg, DamageType and if needed an int with SD-Damage
	 */
	public static final DamageInfo DamageInfo = new DamageInfo();
	
	/**
	 * Sends a text packet
	 * Requires: String actor and message, and if this is a pm, it should be specified with another boolean argument 
	 */
	public static final PlayerSay PlayerSay = new PlayerSay();
	
	/**
	 * Teleport's the client<br>
	 * Current use limited only to transport to regions!<br>
	 * Requires: Region
	 */
	public static final PlayerTeleport PlayerTeleport = new PlayerTeleport();

	/**
	 * Sends to the world this clients intention
	 * Requires: ActionUpdate.#TYPE GameObject animator, GameObject target if needed
	 */
	public static final ActionUpdate ActionUpdate = new ActionUpdate();
	
	/**
	 * Sends a system message
	 * Requires: String message and SystemMessage.{@link MessageType}
	 */
	public static final SystemMessage SystemMessage = new SystemMessage();

	/**
	 * Sends the Death packet
	 * Requires: GameObject whom died and GameObject killer
	 */
	public static final EntityDeath Death = new EntityDeath();
}
