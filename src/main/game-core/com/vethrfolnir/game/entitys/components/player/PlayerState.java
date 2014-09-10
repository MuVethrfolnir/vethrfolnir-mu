/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
