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
package com.vethrfolnir.game.entitys.components.creature;

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
	
	/**
	 * @return
	 */
	public int getRegenTime() {
		return template.RegenTime;
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
