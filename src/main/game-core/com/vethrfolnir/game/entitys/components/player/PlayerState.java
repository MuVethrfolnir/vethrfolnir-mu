/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.entitys.components.player;

import javax.xml.transform.Templates;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.module.DatabaseAccess;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.services.DatabaseService;
import com.vethrfolnir.game.services.dao.PlayerDAO;
import com.vethrfolnir.game.staticdata.ClassId;
import com.vethrfolnir.game.templates.AccountCharacterInfo;
import com.vethrfolnir.game.templates.PlayerTemplate;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.Corax;

/**
 * @author Vlad
 *
 */
@SuppressWarnings("unused")
public class PlayerState implements Component {

	protected MuClient client;
	private GameObject entity;

	protected final int charId;
	protected ClassId classId;
	
	private int accessLevel;
	private int zen;
	
	/**
	 * @param template
	 */
	public PlayerState(PlayerTemplate template) {
		charId = template.charId;
		accessLevel = template.accessLevel;
		classId = ClassId.getClass(template.classId);
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.game.entitys.Component#initialize(com.vethrfolnir.game.entitys.GameObject)
	 */
	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
	}
	
	public void changeClass(ClassId classId) {
		this.classId = classId;
		//TODO Update, capture packet first. maybe its PlayerInfo ?
	}

	/**
	 * @return the classId
	 */
	public ClassId getClassId() {
		return classId;
	}

	/**
	 * @return
	 */
	public int getInventoryExpanded() {
		return 0;
	}
	
	/**
	 * @param zen the zen to set
	 */
	public void setZen(int zen) {
		this.zen = zen;
	}
	
	/**
	 * @return the zen
	 */
	public int getZen() {
		return zen;
	}

	/**
	 * @param accessLevel the accessLevel to set
	 */
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	/**
	 * @return the accessLevel
	 */
	public int getAccessLevel() {
		return accessLevel;
	}
	
	/**
	 * @return
	 */
	public int getCharId() {
		return charId;
	}

	/**
	 * Delecation from MuClient
	 */
	public void sendPacket(WritePacket packet, Object... params) {
		client.sendPacket(packet, params);
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.tools.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		client = null;
		entity = null;
		classId = null;
	}

}
