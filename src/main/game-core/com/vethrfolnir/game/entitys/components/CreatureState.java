/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys.components;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.templates.npc.NpcTemplate;

/**
 * @author Vlad
 *
 */
public class CreatureState implements Component {

	private NpcTemplate template;
	private int effects;

	public CreatureState(NpcTemplate template) {
		this.template = template;
	}
	
	@Override
	public void initialize(GameObject entity) {
	}

	/**
	 * @return the template
	 */
	public NpcTemplate getTemplate() {
		return template;
	}
	
	public int getNpcId() {
		return template.NpcId;
	}
	
	@Override
	public void dispose() {
		template = null;
	}

	/**
	 * @return
	 */
	public int getActiveEffect() {
		return effects;
	}
	
	/**
	 * @param effects the effects to set
	 */
	public void setEffects(int effects) {
		this.effects = effects;
	}

}
